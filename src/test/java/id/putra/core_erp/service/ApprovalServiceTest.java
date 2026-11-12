package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Principal;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.modules.approval.model.ApprovalRequest;
import id.putra.core_erp.modules.approval.repository.ApprovalRequestRepo;
import id.putra.core_erp.modules.approval.repository.ApprovalStepRepo;
import id.putra.core_erp.modules.approval.repository.ApprovalWorkflowRepo;
import id.putra.core_erp.modules.approval.service.ApprovalService;
import id.putra.core_erp.support.TestFixtures;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers the approval workflow engine.
 *
 * <p>
 * The rules here decide whether a document moves forward, completes, or stops.
 * Three are
 * the kind that fail silently in production:
 *
 * <ul>
 * <li>A duplicate submit for the same reference would create two parallel
 * workflows, so
 * both could approve and the budget would be committed twice.</li>
 * <li>Authorisation is checked against the <em>current</em> step, not the next
 * one — an
 * off-by-one would let a supervisor approve the manager's step.</li>
 * <li>The status stays {@code SUBMIT} while steps remain and only becomes
 * {@code COMPLETED} on the last one. Callers key budget deduction off that
 * value.</li>
 * </ul>
 */
@QuarkusTest
class ApprovalServiceTest {

	@Inject
	ApprovalService approvalService;

	@Inject
	ApprovalRequestRepo approvalRequestRepo;

	@Inject
	ApprovalWorkflowRepo approvalWorkflowRepo;

	@Inject
	ApprovalStepRepo approvalStepRepo;

	@Inject
	EntityManager em;

	private static final AtomicInteger SEQ = new AtomicInteger();

	private static String nextRef() {
		return "APR-" + SEQ.incrementAndGet();
	}

	private static SecurityIdentity identityWithRole(String role) {
		SecurityIdentity identity = Mockito.mock(SecurityIdentity.class);
		Principal principal = Mockito.mock(Principal.class);
		BDDMockito.given(principal.getName()).willReturn(role + "-user");
		BDDMockito.given(identity.getPrincipal()).willReturn(principal);
		BDDMockito.given(identity.getRoles()).willReturn(Set.of(role));
		return identity;
	}

	/** A one-step workflow for the given type. */
	private void oneStepWorkflow(ApprovalType type, String role) {
		var wf = TestFixtures.workflow("WF-" + type + "-" + SEQ.incrementAndGet(), type);
		approvalWorkflowRepo.persist(wf);
		approvalStepRepo.persist(TestFixtures.step(wf, 1, role));
		em.flush();
	}

	/** A two-step workflow. */
	private void twoStepWorkflow(ApprovalType type, String firstRole, String secondRole) {
		var wf = TestFixtures.workflow("WF2-" + type + "-" + SEQ.incrementAndGet(), type);
		approvalWorkflowRepo.persist(wf);
		approvalStepRepo.persist(TestFixtures.step(wf, 1, firstRole));
		approvalStepRepo.persist(TestFixtures.step(wf, 2, secondRole));
		em.flush();
	}

	// ------------------------------------------------------------- submit

	@Test
	@TestTransaction
	@DisplayName("submit is refused when no workflow exists for the type")
	void submitRefusedWithoutWorkflow() {
		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> approvalService.submit(identityWithRole("anyone"), ApprovalType.INVOICE, nextRef()));

		assertTrue(ex.getMessage().contains("workflow not found"),
				"a missing workflow must be reported, got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("submit is refused when the workflow has no steps configured")
	void submitRefusedWithoutSteps() {
		var wf = TestFixtures.workflow("WF-NOSTEPS-" + SEQ.incrementAndGet(), ApprovalType.RECEIPT);
		approvalWorkflowRepo.persist(wf);
		em.flush();

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> approvalService.submit(identityWithRole("anyone"), ApprovalType.RECEIPT, nextRef()));

		assertTrue(ex.getMessage().contains("No approval steps configured"),
				"a stepless workflow must fail loudly rather than strand a request, got: "
						+ ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("submit creates a SUBMIT-status request on the first step")
	void submitCreatesRequestOnFirstStep() {
		oneStepWorkflow(ApprovalType.REQUISITION, "supervisor");
		String ref = nextRef();

		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();
		em.clear();

		ApprovalRequest request = approvalRequestRepo.findPendingByReferenceId(ref);

		assertNotNull(request, "the request must be pending after submit");
		assertEquals(ApprovalStatus.SUBMIT, request.status);
		assertEquals(1, request.step.stepOrder, "the request must start on the first step");
		assertEquals("supervisor-user", request.submittedBy, "the submitter must be recorded");
	}

	@Test
	@TestTransaction
	@DisplayName("a second submit for the same reference is refused")
	void duplicateSubmitIsRefused() {
		oneStepWorkflow(ApprovalType.REQUISITION, "supervisor");
		String ref = nextRef();

		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref));

		assertTrue(ex.getMessage().contains("already pending"),
				"a duplicate submit would create two parallel workflows, got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("submit records a history row for the SUBMIT action")
	void submitRecordsHistory() {
		oneStepWorkflow(ApprovalType.REQUISITION, "supervisor");
		String ref = nextRef();

		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();
		em.clear();

		ApprovalRequest request = approvalRequestRepo.findPendingByReferenceId(ref);
		assertEquals(1, request.histories.size(), "submit must leave exactly one history row");
		assertEquals(ApprovalStatus.SUBMIT, request.histories.get(0).action);
	}

	// ------------------------------------------------------------ approve

	@Test
	@TestTransaction
	@DisplayName("approving the last step completes the request")
	void approvingLastStepCompletes() {
		oneStepWorkflow(ApprovalType.REQUISITION, "supervisor");
		String ref = nextRef();
		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		ApprovalStatus result = approvalService.approve(
				identityWithRole("supervisor"), ApprovalType.REQUISITION, ref, "ok");

		assertEquals(ApprovalStatus.COMPLETED, result,
				"one-step workflows must complete on the first approval — callers key budget deduction off this");
	}

	@Test
	@TestTransaction
	@DisplayName("approving an intermediate step keeps the status SUBMIT and advances the step")
	void approvingIntermediateStepKeepsSubmit() {
		twoStepWorkflow(ApprovalType.REQUISITION, "supervisor", "manager");
		String ref = nextRef();
		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		ApprovalStatus result = approvalService.approve(
				identityWithRole("supervisor"), ApprovalType.REQUISITION, ref, "forwarding");
		em.flush();
		em.clear();

		assertEquals(ApprovalStatus.SUBMIT, result,
				"an intermediate approval must stay SUBMIT so the document is not treated as done");
		ApprovalRequest request = approvalRequestRepo.findPendingByReferenceId(ref);
		assertEquals(2, request.step.stepOrder, "the step pointer must advance to the next step");
	}

	@Test
	@TestTransaction
	@DisplayName("the wrong role cannot approve the current step")
	void wrongRoleCannotApprove() {
		oneStepWorkflow(ApprovalType.REQUISITION, "supervisor");
		String ref = nextRef();
		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> approvalService.approve(identityWithRole("intern"), ApprovalType.REQUISITION, ref, "ok"));

		assertTrue(ex.getMessage().contains("not authorized"),
				"an unauthorised approver must be rejected, got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("authorisation is checked against the current step, not the next one")
	void authorisationTargetsCurrentStep() {
		twoStepWorkflow(ApprovalType.REQUISITION, "supervisor", "manager");
		String ref = nextRef();
		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		// The manager role is valid for step 2, but step 1 is still current. An
		// off-by-one
		// in the authorisation check would wrongly let the manager skip ahead.
		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> approvalService.approve(identityWithRole("manager"), ApprovalType.REQUISITION, ref, "ok"));

		assertTrue(ex.getMessage().contains("not authorized"),
				"a role valid only for a later step must not approve the current one, got: "
						+ ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("approving with a mismatched type is refused")
	void approveRefusedOnTypeMismatch() {
		oneStepWorkflow(ApprovalType.REQUISITION, "supervisor");
		String ref = nextRef();
		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> approvalService.approve(identityWithRole("supervisor"), ApprovalType.QUOTATION, ref, "ok"));

		assertTrue(ex.getMessage().contains("type mismatch"),
				"approving under the wrong type must be refused, got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("the whole workflow can be walked to COMPLETED, leaving a history row per transition")
	void fullWorkflowCompletes() {
		twoStepWorkflow(ApprovalType.REQUISITION, "supervisor", "manager");
		String ref = nextRef();
		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		approvalService.approve(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref, "step 1 ok");
		em.flush();
		ApprovalStatus finalStatus = approvalService.approve(
				identityWithRole("manager"), ApprovalType.REQUISITION, ref, "step 2 ok");
		em.flush();
		em.clear();

		assertEquals(ApprovalStatus.COMPLETED, finalStatus, "the last step must complete the request");

		ApprovalRequest request = approvalRequestRepo.find("referenceId", ref).firstResult();
		assertEquals(ApprovalStatus.COMPLETED, request.status);
		assertEquals(3, request.histories.size(),
				"every transition must leave a history row: submit, approve, complete");
	}

	// ------------------------------------------------------------- reject

	@Test
	@TestTransaction
	@DisplayName("reject requires a non-blank comment")
	void rejectRequiresComment() {
		oneStepWorkflow(ApprovalType.REQUISITION, "supervisor");
		String ref = nextRef();
		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		RuntimeException blank = assertThrows(RuntimeException.class,
				() -> approvalService.reject(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref, "  "));
		assertTrue(blank.getMessage().contains("comment is required"), "got: " + blank.getMessage());

		RuntimeException nullComment = assertThrows(RuntimeException.class,
				() -> approvalService.reject(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref, null));
		assertTrue(nullComment.getMessage().contains("comment is required"), "got: " + nullComment.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("reject sets the request to REJECTED and records the reason")
	void rejectSetsStatusAndReason() {
		oneStepWorkflow(ApprovalType.REQUISITION, "supervisor");
		String ref = nextRef();
		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		approvalService.reject(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref, "over budget");
		em.flush();
		em.clear();

		ApprovalRequest request = approvalRequestRepo.find("referenceId", ref).firstResult();
		assertEquals(ApprovalStatus.REJECTED, request.status);
		assertEquals("over budget", request.histories.get(request.histories.size() - 1).comment,
				"the rejection reason must be recorded for the audit trail");
	}

	@Test
	@TestTransaction
	@DisplayName("the wrong role cannot reject the current step")
	void wrongRoleCannotReject() {
		oneStepWorkflow(ApprovalType.REQUISITION, "supervisor");
		String ref = nextRef();
		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> approvalService.reject(identityWithRole("intern"), ApprovalType.REQUISITION, ref, "no"));

		assertTrue(ex.getMessage().contains("not authorized"), "got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("a rejected request is no longer pending, so it cannot be approved afterwards")
	void rejectedRequestIsNoLongerPending() {
		oneStepWorkflow(ApprovalType.REQUISITION, "supervisor");
		String ref = nextRef();
		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		approvalService.reject(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref, "no");
		em.flush();
		em.clear();

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> approvalService.approve(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref, "ok"));

		assertTrue(ex.getMessage().contains("No pending approval request"),
				"a rejected request must not be approvable, got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("rejecting an unknown reference is refused")
	void rejectRefusedForUnknownReference() {
		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> approvalService.reject(identityWithRole("supervisor"), ApprovalType.REQUISITION,
						"APR-DOES-NOT-EXIST", "no"));

		assertTrue(ex.getMessage().contains("No pending approval request"), "got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("approving an unknown reference is refused")
	void approveRefusedForUnknownReference() {
		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> approvalService.approve(identityWithRole("supervisor"), ApprovalType.REQUISITION,
						"APR-DOES-NOT-EXIST", "ok"));

		assertTrue(ex.getMessage().contains("No pending approval request"), "got: " + ex.getMessage());
	}

	@Test
	@TestTransaction
	@DisplayName("rejecting with a mismatched type is refused")
	void rejectRefusedOnTypeMismatch() {
		oneStepWorkflow(ApprovalType.REQUISITION, "supervisor");
		String ref = nextRef();
		approvalService.submit(identityWithRole("supervisor"), ApprovalType.REQUISITION, ref);
		em.flush();

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> approvalService.reject(identityWithRole("supervisor"), ApprovalType.QUOTATION, ref, "no"));

		assertTrue(ex.getMessage().contains("type mismatch"), "got: " + ex.getMessage());
	}
}
