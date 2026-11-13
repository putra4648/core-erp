package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.modules.approval.repository.ApprovalStepRepo;
import id.putra.core_erp.modules.approval.repository.ApprovalWorkflowRepo;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.repository.BudgetRepo;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.model.RequisitionItem;
import id.putra.core_erp.modules.procurement.repository.RequisitionItemRepo;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import id.putra.core_erp.modules.procurement.service.RequisitionService;
import id.putra.core_erp.support.TestFixtures;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers the business rules in {@link RequisitionService}.
 *
 * <p>
 * This is the reference implementation for the layering rules, and it carries
 * the budget
 * invariant: a requisition may only be submitted if its budget still covers it,
 * and the
 * budget is deducted <em>once</em>, at the final approval step — not on every
 * intermediate
 * approval. Getting that wrong either lets overspend through or deducts the
 * same amount
 * several times, and neither shows up until the numbers are reconciled.
 *
 * <p>
 * {@code @QuarkusTest} rather than Mockito because the service depends on
 * {@code ApprovalService}, and the approver's roles drive authorisation. The
 * identity is
 * built by hand in {@link #identityWithRole} so one test can act as several
 * approvers in
 * sequence, which is the only way to walk a multi-step workflow in a single
 * transaction.
 */
@QuarkusTest
class RequisitionServiceTest {

	@Inject
	RequisitionService requisitionService;

	@Inject
	RequisitionRepo requisitionRepo;

	@Inject
	RequisitionItemRepo requisitionItemRepo;

	@Inject
	DepartmentRepo departmentRepo;

	@Inject
	BudgetRepo budgetRepo;

	@Inject
	ApprovalWorkflowRepo approvalWorkflowRepo;

	@Inject
	ApprovalStepRepo approvalStepRepo;

	@Inject
	EntityManager em;

	/**
	 * Unique per call so fixtures in different tests never collide on referenceId.
	 */
	private static final AtomicInteger SEQ = new AtomicInteger();

	private static String nextRef(String prefix) {
		return prefix + "-" + SEQ.incrementAndGet();
	}

	private Department newDepartment(String prefix) {
		Department d = TestFixtures.department(prefix + SEQ.incrementAndGet());
		departmentRepo.persist(d);
		return d;
	}

	private Budget newBudget(Department dept, String allocated) {
		Budget b = TestFixtures.budget(dept, "Budget " + dept.code, allocated);
		budgetRepo.persist(b);
		return b;
	}

	/** A requisition with one line, persisted with its item, then reloaded. */
	private Requisition newRequisition(Department dept, Budget budget, String unitPrice, int qty) {
		String ref = nextRef("REQ");
		Requisition r = TestFixtures.requisition(ref, dept, budget);
		r.referenceId = ref;
		requisitionRepo.persist(r);

		RequisitionItem item = TestFixtures.requisitionItem(r, "Item " + ref, qty, unitPrice);
		requisitionItemRepo.persist(item);
		em.flush();
		em.clear();

		return requisitionRepo.find("referenceId", ref).firstResult();
	}

	/**
	 * Builds a {@code SecurityIdentity} with one role.
	 *
	 * <p>
	 * Hand-built rather than injected so a single test can act as several different
	 * approvers in sequence — the only way to walk a multi-step workflow inside one
	 * rollback.
	 */
	private static SecurityIdentity identityWithRole(String role) {
		SecurityIdentity identity = org.mockito.Mockito.mock(SecurityIdentity.class);
		java.security.Principal principal = org.mockito.Mockito.mock(java.security.Principal.class);
		org.mockito.BDDMockito.given(principal.getName()).willReturn(role + "-user");
		org.mockito.BDDMockito.given(identity.getPrincipal()).willReturn(principal);
		org.mockito.BDDMockito.given(identity.getRoles()).willReturn(Set.of(role));
		return identity;
	}

	// ---------------------------------------------------------------- guards

	@Test
	@TestTransaction
	@DisplayName("save is rejected — callers must use submit so the workflow runs")
	void saveIsRejected() {
		assertThrows(UnsupportedOperationException.class, () -> requisitionService.save(new Requisition()),
				"save must be blocked; bypassing submit would skip approval entirely");
	}

	@Test
	@TestTransaction
	@DisplayName("update is rejected")
	void updateIsRejected() {
		assertThrows(UnsupportedOperationException.class, () -> requisitionService.update(new Requisition()),
				"update must be blocked so a requisition cannot be mutated outside the workflow");
	}

	@Test
	@TestTransaction
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		Department dept = newDepartment("RSD");
		Budget budget = newBudget(dept, "1000.00");
		Requisition r = newRequisition(dept, budget, "10.00", 1);

		Long id = r.id;
		requisitionService.delete(id);
		em.flush();
		em.clear();

		assertTrue(requisitionRepo.findByIdOptional(id).isEmpty(),
				"the requisition must be hidden after a soft delete");

		Object flag = em.createNativeQuery("select deleted from requisition where id = ?1")
				.setParameter(1, id).getSingleResult();
		assertEquals(Boolean.TRUE, flag, "the row must remain with the flag flipped, not be deleted");
	}

	@Test
	@TestTransaction
	@DisplayName("count excludes soft-deleted rows")
	void countExcludesSoftDeleted() {
		Department dept = newDepartment("RSC");
		Budget budget = newBudget(dept, "1000.00");
		Requisition live = newRequisition(dept, budget, "10.00", 1);
		Requisition dead = newRequisition(dept, budget, "10.00", 1);

		long before = requisitionService.count();

		requisitionService.delete(dead.id);
		em.flush();
		em.clear();

		assertEquals(before - 1, requisitionService.count(),
				"count must drop by exactly one after a soft delete");
		assertTrue(requisitionRepo.findByIdOptional(live.id).isPresent());
	}

	// ------------------------------------------------------ submit + budget

	@Test
	@TestTransaction
	@DisplayName("submit is refused when the budget does not cover the line items")
	void submitRefusedWhenBudgetTooSmall() {
		Department dept = newDepartment("RSB");
		Budget budget = newBudget(dept, "100.00");
		// 10 x 50 = 500, comfortably over the 100 allocated.
		Requisition r = newRequisition(dept, budget, "50.00", 10);

		Requisition toSubmit = requisitionRepo.findById(r.id);

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> requisitionService.submit(toSubmit, identityWithRole("requestor")));

		assertTrue(ex.getMessage().contains("Budget not available"),
				"the failure must name the budget shortfall, got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("submit is refused when the requisition has no budget attached")
	void submitRefusedWithoutBudget() {
		Department dept = newDepartment("RSN");
		Budget budget = newBudget(dept, "1000.00");
		Requisition r = newRequisition(dept, budget, "10.00", 1);

		// The entity is managed, so clearing the association is visible to the service
		// without a flush. Flushing here would fail first on the entity's own @NotNull
		// budget constraint and the assertion below would test Hibernate, not the service.
		Requisition toSubmit = requisitionRepo.findById(r.id);
		toSubmit.budget = null;

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> requisitionService.submit(toSubmit, identityWithRole("requestor")));

		assertTrue(ex.getMessage().contains("Budget is required"),
				"a missing budget must be reported explicitly, got: " + ex.getMessage());
	}

	// ------------------------------------------- approve / reject status guard

	@Test
	@TestTransaction
	@DisplayName("approve is refused when the requisition is not in SUBMIT status")
	void approveRefusedWhenNotSubmitted() {
		Department dept = newDepartment("RSA");
		Budget budget = newBudget(dept, "1000.00");
		Requisition r = newRequisition(dept, budget, "10.00", 1);

		Requisition loaded = requisitionRepo.findById(r.id);
		loaded.status = ApprovalStatus.APPROVED;
		requisitionRepo.persist(loaded);
		em.flush();

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> requisitionService.approve(loaded.id, identityWithRole("supervisor")));

		assertTrue(ex.getMessage().contains("not in submit status"),
				"a non-SUBMIT requisition must not be approvable, got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("approve is refused for an unknown id")
	void approveRefusedForUnknownId() {
		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> requisitionService.approve(999_999L, identityWithRole("supervisor")));

		assertTrue(ex.getMessage().contains("not found"), "got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("reject is refused when the requisition is not in SUBMIT status")
	void rejectRefusedWhenNotSubmitted() {
		Department dept = newDepartment("RSR");
		Budget budget = newBudget(dept, "1000.00");
		Requisition r = newRequisition(dept, budget, "10.00", 1);

		Requisition loaded = requisitionRepo.findById(r.id);
		loaded.status = ApprovalStatus.REJECTED;
		requisitionRepo.persist(loaded);
		em.flush();

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> requisitionService.reject(loaded.id, identityWithRole("supervisor"), "no"));

		assertTrue(ex.getMessage().contains("not in submit status"), "got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("reject is refused for an unknown id")
	void rejectRefusedForUnknownId() {
		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> requisitionService.reject(999_999L, identityWithRole("supervisor"), "no"));

		assertTrue(ex.getMessage().contains("not found"), "got: " + ex.getMessage());
	}

	// ---------------------------------------------- budget deduction invariants

	@Test
	@TestTransaction
	@DisplayName("budget is deducted once, at the final step, not on an intermediate approval")
	void budgetDeductedOnlyAtFinalStep() {
		Department dept = newDepartment("RSF");
		Budget budget = newBudget(dept, "1000.00");
		Requisition r = newRequisition(dept, budget, "100.00", 2); // 200 total

		// Two-step workflow: supervisor, then manager.
		var wf = TestFixtures.workflow("WF " + dept.code, ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(wf);
		approvalStepRepo.persist(TestFixtures.step(wf, 1, "supervisor"));
		approvalStepRepo.persist(TestFixtures.step(wf, 2, "manager"));
		em.flush();

		Long budgetId = budget.id;
		Requisition loaded = requisitionRepo.findById(r.id);

		requisitionService.submit(loaded, identityWithRole("supervisor"));

		assertEquals(0, new BigDecimal("1000.00").compareTo(budgetRepo.findById(budgetId).allocated),
				"submitting must not deduct — the pre-check only validates");

		requisitionService.approve(loaded.id, identityWithRole("supervisor"));

		assertEquals(0, new BigDecimal("1000.00").compareTo(budgetRepo.findById(budgetId).allocated),
				"an intermediate approval must NOT deduct; deduction happens only at COMPLETED");

		requisitionService.approve(loaded.id, identityWithRole("manager"));

		assertEquals(0, new BigDecimal("800.00").compareTo(budgetRepo.findById(budgetId).allocated),
				"the budget must be deducted exactly once, by the total, at the final step");
		assertEquals(ApprovalStatus.APPROVED, requisitionRepo.findById(loaded.id).status,
				"the requisition must be APPROVED after the last step");
	}

	@Test
	@TestTransaction
	@DisplayName("rejecting a submitted requisition marks it REJECTED and leaves the budget untouched")
	void rejectLeavesBudgetUntouched() {
		Department dept = newDepartment("RSJ");
		Budget budget = newBudget(dept, "1000.00");
		Requisition r = newRequisition(dept, budget, "100.00", 2);

		var wf = TestFixtures.workflow("WF " + dept.code, ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(wf);
		approvalStepRepo.persist(TestFixtures.step(wf, 1, "supervisor"));
		em.flush();

		Long budgetId = budget.id;
		Requisition loaded = requisitionRepo.findById(r.id);
		requisitionService.submit(loaded, identityWithRole("supervisor"));

		requisitionService.reject(loaded.id, identityWithRole("supervisor"), "over budget this quarter");

		assertEquals(ApprovalStatus.REJECTED, requisitionRepo.findById(loaded.id).status);
		assertEquals(0, new BigDecimal("1000.00").compareTo(budgetRepo.findById(budgetId).allocated),
				"a rejection must not deduct — nothing was committed");
	}
}
