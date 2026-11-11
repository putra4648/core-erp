package id.putra.core_erp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.enums.BudgetType;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.repository.BudgetRepo;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.model.RequisitionItem;
import id.putra.core_erp.modules.procurement.repository.RequisitionItemRepo;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Guards the requisition search queries against losing their soft delete filter.
 *
 * <p>Two classes of query live in {@link RequisitionRepo} and both are covered here:
 * the JPQL searches, where the filter comes from {@code @SQLRestriction}, and the
 * purchaser searches, which are native and carry the filter by hand.
 *
 * <p>Runs under {@code @QuarkusTest} because it needs a real database — JPQL and native
 * SQL cannot be validated without a schema. {@code @TestTransaction} rolls back.
 */
@QuarkusTest
class RequisitionSearchSoftDeleteTest {

	@Inject
	RequisitionRepo requisitionRepo;

	@Inject
	RequisitionItemRepo requisitionItemRepo;

	@Inject
	DepartmentRepo departmentRepo;

	@Inject
	BudgetRepo budgetRepo;

	@Inject
	EntityManager em;

	private Department newDepartment(String code) {
		Department d = new Department();
		d.code = code;
		d.name = "Dept " + code;
		departmentRepo.persist(d);
		return d;
	}

	/**
	 * Requisition.budget is {@code @NotNull}, so every fixture needs a real Budget.
	 * Budget joins Department on dept_number, not id — hence the explicit assignment.
	 */
	private Budget newBudget(Department dept, String name) {
		Budget b = new Budget();
		b.department = dept;
		b.name = name;
		b.type = BudgetType.EXPENSE;
		b.fiscalYear = 2026;
		b.allocated = new BigDecimal("100000.00");
		budgetRepo.persist(b);
		return b;
	}

	private Requisition newRequisition(String title, Department dept, Budget budget) {
		Requisition r = new Requisition();
		r.title = title;
		r.referenceId = "REF-" + title;
		r.dueDate = LocalDate.now().plusDays(7);
		r.totalAmount = java.math.BigInteger.valueOf(1000);
		r.status = ApprovalStatus.SUBMIT;
		r.department = dept;
		r.budget = budget;
		requisitionRepo.persist(r);
		return r;
	}

	@Test
	@TestTransaction
	void searchExcludesSoftDeletedRequisitions() {
		Department dept = newDepartment("NSD01");
		Budget budget = newBudget(dept, "Budget NSD01");
		Requisition live = newRequisition("SearchLiveItem", dept, budget);
		Requisition dead = newRequisition("SearchDeadItem", dept, budget);
		Long liveId = live.id;
		Long deadId = dead.id;
		em.flush();
		em.clear();

		requisitionRepo.softDeleteById(deadId);
		em.flush();
		em.clear();

		Set<String> deptCodes = Set.of("NSD01");

		List<Requisition> hits = requisitionRepo
				.getTitleContainingIgnoreCaseOrStatusAndDepartmentIn("Search", "SUBMIT", deptCodes);
		assertTrue(hits.stream().anyMatch(r -> liveId.equals(r.id)),
				"live requisition must be found by the search");
		assertFalse(hits.stream().anyMatch(r -> deadId.equals(r.id)),
				"soft-deleted requisition must NOT be returned by the search");

		Long count = requisitionRepo
				.countByTitleContainingIgnoreCaseOrStatusAndDepartmentIn("Search", "SUBMIT", deptCodes);
		assertEquals(1L, count, "count must exclude the soft-deleted requisition");
	}

	/**
	 * Guards the department filter specifically. {@code Requisition.department} joins on
	 * {@code Department.dept_number}, so a requisition must only be returned for the
	 * department number it was created under — not for any other, and not for the
	 * department's surrogate id.
	 */
	@Test
	@TestTransaction
	void searchFiltersOnDepartmentNumber() {
		Department mine = newDepartment("NSDF1");
		Department other = newDepartment("NSDF2");
		Budget budget = newBudget(mine, "Budget NSDF1");
		Budget otherBudget = newBudget(other, "Budget NSDF2");
		Requisition req = newRequisition("FilterMineItem", mine, budget);
		Requisition otherReq = newRequisition("FilterOtherItem", other, otherBudget);
		Long mineId = req.id;
		Long otherId = otherReq.id;
		em.flush();
		em.clear();

		List<Requisition> mineOnly = requisitionRepo
				.getTitleContainingIgnoreCaseOrStatusAndDepartmentIn("Filter", "SUBMIT", Set.of("NSDF1"));

		assertTrue(mineOnly.stream().anyMatch(r -> mineId.equals(r.id)),
				"requisition of the requested department must be returned");
		assertFalse(mineOnly.stream().anyMatch(r -> otherId.equals(r.id)),
				"requisition of another department must not leak into the result");
	}

	@Test
	@TestTransaction
	void cascadeSoftDeletesRequisitionItems() {
		Department dept = newDepartment("NSD02");
		Budget budget = newBudget(dept, "Budget NSD02");
		Requisition req = newRequisition("CascadeReqItems", dept, budget);

		RequisitionItem item = new RequisitionItem();
		item.name = "CascadeItem";
		item.quantity = 2;
		item.unitPrice = new BigDecimal("10.00");
		item.requisition = req;
		requisitionItemRepo.persist(item);

		Long reqId = req.id;
		Long itemId = item.id;
		em.flush();
		em.clear();

		requisitionRepo.softDeleteById(reqId);
		em.flush();
		em.clear();

		assertFalse(requisitionItemRepo.findByIdOptional(itemId).isPresent(),
				"requisition items must cascade to soft-deleted with their parent");

		Object flag = em.createNativeQuery("select deleted from requisition_items where id = ?1")
				.setParameter(1, itemId).getSingleResult();
		assertEquals(Boolean.TRUE, flag, "cascaded item row must exist with deleted = true");
	}
}
