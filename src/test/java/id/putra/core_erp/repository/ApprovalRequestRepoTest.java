package id.putra.core_erp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.modules.approval.model.ApprovalRequest;
import id.putra.core_erp.modules.approval.model.ApprovalStep;
import id.putra.core_erp.modules.approval.model.ApprovalWorkflow;
import id.putra.core_erp.modules.approval.repository.ApprovalRequestRepo;
import id.putra.core_erp.modules.approval.repository.ApprovalStepRepo;
import id.putra.core_erp.modules.approval.repository.ApprovalWorkflowRepo;
import id.putra.core_erp.support.TestFixtures;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers {@link ApprovalRequestRepo}.
 *
 * <p>
 * {@code findPendingByReferenceId} is the single lookup behind both approve and
 * reject,
 * and it filters on {@code status = SUBMIT}. That filter is load-bearing: a
 * request already
 * COMPLETED or REJECTED must not be returned, or a finished approval could be
 * actioned a
 * second time and the workflow would advance twice.
 *
 * <p>
 * Unlike the rest of the approval module, {@link ApprovalRequest} is the audit
 * trail and
 * does not extend {@code BaseEntity} — it hard-deletes by design. There is
 * therefore no
 * soft-delete behaviour to assert here.
 */
@QuarkusTest
class ApprovalRequestRepoTest {

	@Inject
	ApprovalRequestRepo approvalRequestRepo;

	@Inject
	ApprovalWorkflowRepo approvalWorkflowRepo;

	@Inject
	ApprovalStepRepo approvalStepRepo;

	@Inject
	EntityManager em;

	private ApprovalRequest newRequest(String referenceId, ApprovalWorkflow wf, ApprovalStep step,
			ApprovalStatus status) {
		ApprovalRequest r = new ApprovalRequest();
		r.referenceId = referenceId;
		r.workflow = wf;
		r.step = step;
		r.status = status;
		r.submittedBy = "tester";
		return r;
	}

	@Test
	@TestTransaction
	@DisplayName("findPendingByReferenceId returns the SUBMIT-status request")
	void findsPendingRequest() {
		ApprovalWorkflow wf = TestFixtures.workflow("WF Pending", ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(wf);
		ApprovalStep step = TestFixtures.step(wf, 1, "SUPERVISOR");
		approvalStepRepo.persist(step);
		ApprovalRequest req = newRequest("REF-PENDING", wf, step, ApprovalStatus.SUBMIT);
		approvalRequestRepo.persist(req);
		em.flush();

		ApprovalRequest found = approvalRequestRepo.findPendingByReferenceId("REF-PENDING");

		assertSame(req.id, found.id, "the pending request must be found by its reference id");
	}

	@Test
	@TestTransaction
	@DisplayName("a COMPLETED request is not returned as pending")
	void completedRequestIsNotPending() {
		ApprovalWorkflow wf = TestFixtures.workflow("WF Completed", ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(wf);
		ApprovalStep step = TestFixtures.step(wf, 1, "SUPERVISOR");
		approvalStepRepo.persist(step);
		approvalRequestRepo.persist(newRequest("REF-DONE", wf, step, ApprovalStatus.COMPLETED));
		em.flush();
		em.clear();

		assertNull(approvalRequestRepo.findPendingByReferenceId("REF-DONE"),
				"a completed request must not be actioned again");
	}

	@Test
	@TestTransaction
	@DisplayName("a mid-workflow request is still pending, with its step pointer advanced")
	void approvedMidWorkflowRequestIsPending() {
		ApprovalWorkflow wf = TestFixtures.workflow("WF MidFlow", ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(wf);
		ApprovalStep first = TestFixtures.step(wf, 1, "SUPERVISOR");
		approvalStepRepo.persist(first);
		ApprovalStep second = TestFixtures.step(wf, 2, "MANAGER");
		approvalStepRepo.persist(second);

		// Mid-workflow the request stays SUBMIT while the step pointer advances — that
		// is
		// what ApprovalService.approve does, and why this lookup still finds it.
		ApprovalRequest req = newRequest("REF-MIDFLOW", wf, second, ApprovalStatus.SUBMIT);
		approvalRequestRepo.persist(req);
		em.flush();
		em.clear();

		ApprovalRequest found = approvalRequestRepo.findPendingByReferenceId("REF-MIDFLOW");

		assertEquals(ApprovalStatus.SUBMIT, found.status);
		assertSame(second.id, found.step.id, "the step pointer must still point at the current step");
	}

	@Test
	@TestTransaction
	@DisplayName("a REJECTED request is not returned as pending")
	void rejectedRequestIsNotPending() {
		ApprovalWorkflow wf = TestFixtures.workflow("WF Rejected", ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(wf);
		ApprovalStep step = TestFixtures.step(wf, 1, "SUPERVISOR");
		approvalStepRepo.persist(step);
		approvalRequestRepo.persist(newRequest("REF-REJECTED", wf, step, ApprovalStatus.REJECTED));
		em.flush();
		em.clear();

		assertNull(approvalRequestRepo.findPendingByReferenceId("REF-REJECTED"));
	}

	@Test
	@TestTransaction
	@DisplayName("lookup is scoped to one reference id")
	void lookupIsScopedToReferenceId() {
		ApprovalWorkflow wf = TestFixtures.workflow("WF Scoped", ApprovalType.REQUISITION);
		approvalWorkflowRepo.persist(wf);
		ApprovalStep step = TestFixtures.step(wf, 1, "SUPERVISOR");
		approvalStepRepo.persist(step);

		approvalRequestRepo.persist(newRequest("REF-ONE", wf, step, ApprovalStatus.SUBMIT));
		approvalRequestRepo.persist(newRequest("REF-TWO", wf, step, ApprovalStatus.SUBMIT));
		em.flush();
		em.clear();

		// The whole-table count guards against the lookup ignoring its parameter.
		assertEquals(2L, approvalRequestRepo.count(), "fixture must have persisted both requests");
		assertEquals("REF-TWO", approvalRequestRepo.findPendingByReferenceId("REF-TWO").referenceId);
	}

	@Test
	@TestTransaction
	@DisplayName("an unknown reference id resolves to null, which is how approve fails loudly")
	void unknownReferenceIdResolvesToNull() {
		assertNull(approvalRequestRepo.findPendingByReferenceId("REF-DOES-NOT-EXIST"));
	}
}
