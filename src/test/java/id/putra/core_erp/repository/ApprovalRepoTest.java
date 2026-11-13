package id.putra.core_erp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.modules.approval.model.ApprovalStep;
import id.putra.core_erp.modules.approval.model.ApprovalWorkflow;
import id.putra.core_erp.modules.approval.repository.ApprovalStepRepo;
import id.putra.core_erp.modules.approval.repository.ApprovalWorkflowRepo;
import id.putra.core_erp.support.TestFixtures;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers {@link ApprovalStepRepo} and {@link ApprovalWorkflowRepo}.
 *
 * <p>
 * {@code findFirstByWorkflowId} drives which step a new request starts on, and
 * {@code findByWorkflowIdAndStepOrder} drives whether the workflow advances or
 * completes.
 * Both are ordering-sensitive: if the first-step query ever returned the wrong
 * row, every
 * requisition would start at the wrong approval level and nothing would visibly
 * fail.
 */
@QuarkusTest
class ApprovalRepoTest {

	@Inject
	ApprovalWorkflowRepo approvalWorkflowRepo;

	@Inject
	ApprovalStepRepo approvalStepRepo;

	@Inject
	EntityManager em;

	@Test
	@TestTransaction
	@DisplayName("findByRequestType resolves the workflow for its type")
	void findByRequestTypeResolvesWorkflow() {
		ApprovalWorkflow wf = TestFixtures.workflow("WF Requisition", ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(wf);
		em.flush();

		ApprovalWorkflow found = approvalWorkflowRepo.findByRequestType(ApprovalType.REQUISITION);

		assertSame(wf.id, found.id, "the workflow for the requested type must be returned");
	}

	@Test
	@TestTransaction
	@DisplayName("findFirstByWorkflowId returns the lowest step order, not insertion order")
	void findFirstReturnsLowestStepOrder() {
		ApprovalWorkflow wf = TestFixtures.workflow("WF Ordering", ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(wf);

		// Persist out of order on purpose — insertion order must not decide the result.
		approvalStepRepo.persist(TestFixtures.step(wf, 3, "DIRECTOR"));
		approvalStepRepo.persist(TestFixtures.step(wf, 1, "SUPERVISOR"));
		approvalStepRepo.persist(TestFixtures.step(wf, 2, "MANAGER"));
		em.flush();
		em.clear();

		ApprovalStep first = approvalStepRepo.findFirstByWorkflowId(wf.id);

		assertEquals(1, first.stepOrder, "the first step must be the lowest stepOrder");
		assertEquals("SUPERVISOR", first.roleRequired,
				"the first step must be the supervisor step, not whichever was inserted first");
	}

	@Test
	@TestTransaction
	@DisplayName("findByWorkflowIdAndStepOrder resolves the exact step")
	void findByWorkflowIdAndStepOrderResolvesExactStep() {
		ApprovalWorkflow wf = TestFixtures.workflow("WF Lookup", ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(wf);
		approvalStepRepo.persist(TestFixtures.step(wf, 1, "SUPERVISOR"));
		approvalStepRepo.persist(TestFixtures.step(wf, 2, "MANAGER"));
		em.flush();
		em.clear();

		ApprovalStep second = approvalStepRepo.findByWorkflowIdAndStepOrder(wf.id, 2);

		assertEquals(2, second.stepOrder);
		assertEquals("MANAGER", second.roleRequired);

		assertNull(approvalStepRepo.findByWorkflowIdAndStepOrder(wf.id, 99),
				"an unknown step order must resolve to null, which is what ends the workflow");
	}

	@Test
	@TestTransaction
	@DisplayName("findFirstByWorkflowId is scoped to its own workflow")
	void findFirstIsScopedToWorkflow() {
		ApprovalWorkflow mine = TestFixtures.workflow("WF Mine", ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(mine);
		ApprovalWorkflow other = TestFixtures.workflow("WF Other", ApprovalType.QUOTATION);
		approvalWorkflowRepo.persist(other);

		approvalStepRepo.persist(TestFixtures.step(mine, 1, "MINE-ROLE"));
		approvalStepRepo.persist(TestFixtures.step(other, 1, "OTHER-ROLE"));
		em.flush();
		em.clear();

		ApprovalStep first = approvalStepRepo.findFirstByWorkflowId(mine.id);

		assertEquals("MINE-ROLE", first.roleRequired,
				"another workflow's step must not leak into the result");
	}

	@Test
	@TestTransaction
	@DisplayName("soft-deleting a workflow cascades to its steps")
	void softDeleteCascadesToSteps() {
		ApprovalWorkflow wf = TestFixtures.workflow("WF Cascade", ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(wf);
		ApprovalStep step = TestFixtures.step(wf, 1, "SUPERVISOR");
		approvalStepRepo.persist(step);
		em.flush();

		Long wfId = wf.id;
		Long stepId = step.id;

		approvalWorkflowRepo.softDeleteById(wfId);
		em.flush();
		em.clear();

		assertNull(approvalWorkflowRepo.findById(wfId), "workflow must be hidden");
		assertNull(approvalStepRepo.findById(stepId), "steps must cascade to soft-deleted");

		Object flag = em.createNativeQuery("select deleted from approval_steps where id = ?1")
				.setParameter(1, stepId).getSingleResult();
		assertEquals(Boolean.TRUE, flag, "cascaded step row must exist with deleted = true");
	}

	@Test
	@TestTransaction
	@DisplayName("a soft-deleted workflow is not returned by findByRequestType")
	void softDeletedWorkflowIsHidden() {
		ApprovalWorkflow wf = TestFixtures.workflow("WF Hidden", ApprovalType.INVOICE);
		approvalWorkflowRepo.persist(wf);
		em.flush();

		approvalWorkflowRepo.softDeleteById(wf.id);
		em.flush();
		em.clear();

		assertNull(approvalWorkflowRepo.findByRequestType(ApprovalType.INVOICE),
				"a soft-deleted workflow must not be resolvable, so submit fails loudly");
	}
}
