package id.putra.core_erp.modules.approval.service;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.modules.approval.model.ApprovalHistory;
import id.putra.core_erp.modules.approval.model.ApprovalRequest;
import id.putra.core_erp.modules.approval.model.ApprovalStep;
import id.putra.core_erp.modules.approval.model.ApprovalWorkflow;
import io.quarkus.security.identity.SecurityIdentity;
import id.putra.core_erp.modules.approval.repository.ApprovalRequestRepo;
import id.putra.core_erp.modules.approval.repository.ApprovalStepRepo;
import id.putra.core_erp.modules.approval.repository.ApprovalWorkflowRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ApprovalService {
    private final ApprovalRequestRepo approvalRequestRepo;
    private final ApprovalWorkflowRepo approvalWorkflowRepo;
    private final ApprovalStepRepo approvalStepRepo;

    public ApprovalService(
            ApprovalRequestRepo approvalRequestRepo,
            ApprovalWorkflowRepo approvalWorkflowRepo,
            ApprovalStepRepo approvalStepRepo) {
        this.approvalRequestRepo = approvalRequestRepo;
        this.approvalWorkflowRepo = approvalWorkflowRepo;
        this.approvalStepRepo = approvalStepRepo;
    }

    @Transactional
    public void submit(SecurityIdentity identity, ApprovalType type, String referenceId) {
        // check master approval by request type
        ApprovalWorkflow approvalWorkflow = approvalWorkflowRepo.findByRequestType(type);
        if (approvalWorkflow == null) {
            throw new RuntimeException("Approval workflow not found for type: " + type);
        }

        // Prevent duplicate pending requests for the same referenceId
        ApprovalRequest existing = approvalRequestRepo.findPendingByReferenceId(referenceId);
        if (existing != null) {
            throw new RuntimeException("Approval request already pending for referenceId: " + referenceId);
        }

        // Find the first step in the workflow (lowest stepOrder)
        ApprovalStep firstStep = approvalStepRepo.findFirstByWorkflowId(approvalWorkflow.id);
        if (firstStep == null) {
            throw new RuntimeException("No approval steps configured for workflow: " + approvalWorkflow.workflowName);
        }

        var newApprovalRequest = new ApprovalRequest();
        newApprovalRequest.referenceId = referenceId;
        newApprovalRequest.step = firstStep;
        newApprovalRequest.workflow = approvalWorkflow;
        newApprovalRequest.status = ApprovalStatus.SUBMIT;
        newApprovalRequest.submittedBy = identity.getPrincipal().getName();

        var history = new ApprovalHistory();
        history.action = ApprovalStatus.SUBMIT;
        history.actedBy = identity.getPrincipal().getName();
        history.comment = null;
        history.step = firstStep;
        history.request = newApprovalRequest;  // set back-reference for FK

        newApprovalRequest.histories.add(history);
        approvalRequestRepo.persist(newApprovalRequest);
    }

    @Transactional
    public ApprovalStatus approve(SecurityIdentity identity, ApprovalType type, String referenceId, String comment) {
        ApprovalRequest approvalRequest = approvalRequestRepo.findPendingByReferenceId(referenceId);
        if (approvalRequest == null) {
            throw new RuntimeException("No pending approval request found for referenceId: " + referenceId);
        }

        // Verify the request's workflow matches the expected type
        if (approvalRequest.workflow.requestType != type) {
            throw new RuntimeException("Approval request type mismatch: expected " + type
                    + " but found " + approvalRequest.workflow.requestType);
        }

        // Verify the current user is authorized to approve the CURRENT step (not the next one)
        ApprovalStep currentStep = approvalRequest.step;
        if (!isAuthorized(identity, currentStep)) {
            throw new RuntimeException("User is not authorized to approve this request");
        }

        // Check if there is a next step in the workflow
        ApprovalStep nextStep = approvalStepRepo.findByWorkflowIdAndStepOrder(
                currentStep.workflow.id, currentStep.stepOrder + 1);

        var history = new ApprovalHistory();
        history.actedBy = identity.getPrincipal().getName();
        history.comment = comment;
        history.step = currentStep;
        history.request = approvalRequest;

        if (nextStep != null) {
            // More steps remain — advance to next step, keep status SUBMIT
            approvalRequest.status = ApprovalStatus.SUBMIT;
            approvalRequest.step = nextStep;
            history.action = ApprovalStatus.APPROVED;
        } else {
            // All steps completed
            approvalRequest.status = ApprovalStatus.COMPLETED;
            history.action = ApprovalStatus.COMPLETED;
        }

        approvalRequest.histories.add(history);
        approvalRequestRepo.persist(approvalRequest);
        return approvalRequest.status;
    }

    @Transactional
    public void reject(SecurityIdentity identity, ApprovalType type, String referenceId, String comment) {
        if (comment == null || comment.isBlank()) {
            throw new RuntimeException("Rejection comment is required");
        }

        ApprovalRequest approvalRequest = approvalRequestRepo.findPendingByReferenceId(referenceId);
        if (approvalRequest == null) {
            throw new RuntimeException("No pending approval request found for referenceId: " + referenceId);
        }

        // Verify the request's workflow matches the expected type
        if (approvalRequest.workflow.requestType != type) {
            throw new RuntimeException("Approval request type mismatch: expected " + type
                    + " but found " + approvalRequest.workflow.requestType);
        }

        // Verify the current user is authorized to reject the CURRENT step
        ApprovalStep currentStep = approvalRequest.step;
        if (!isAuthorized(identity, currentStep)) {
            throw new RuntimeException("User is not authorized to reject this request");
        }

        approvalRequest.status = ApprovalStatus.REJECTED;

        var history = new ApprovalHistory();
        history.action = ApprovalStatus.REJECTED;
        history.actedBy = identity.getPrincipal().getName();
        history.comment = comment;
        history.step = currentStep;
        history.request = approvalRequest;

        approvalRequest.histories.add(history);
        approvalRequestRepo.persist(approvalRequest);
    }

    /**
     * Check whether the given identity has the role required by the step.
     * Comparison is case-insensitive and uses substring matching.
     */
    private boolean isAuthorized(SecurityIdentity identity, ApprovalStep step) {
        if (step == null || step.roleRequired == null) {
            return false;
        }
        String required = step.roleRequired.toLowerCase();
        return identity.getRoles().stream()
                .anyMatch(role -> role.toLowerCase().contains(required));
    }
}
