package id.putra.core_erp.modules.approval.service;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.modules.approval.model.ApprovalHistory;
import id.putra.core_erp.modules.approval.model.ApprovalRequest;
import id.putra.core_erp.modules.approval.model.ApprovalStep;
import id.putra.core_erp.modules.approval.model.ApprovalWorkflow;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.ConsumeEvent;
import id.putra.core_erp.modules.approval.repository.ApprovalHistoryRepo;
import id.putra.core_erp.modules.approval.repository.ApprovalRequestRepo;
import id.putra.core_erp.modules.approval.repository.ApprovalStepRepo;
import id.putra.core_erp.modules.approval.repository.ApprovalWorkflowRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ApprovalService {
    private final ApprovalHistoryRepo approvalHistoryRepo;
    private final ApprovalRequestRepo approvalRequestRepo;
    private final ApprovalWorkflowRepo approvalWorkflowRepo;
    private final ApprovalStepRepo approvalStepRepo;

    public ApprovalService(
            ApprovalHistoryRepo approvalHistoryRepo,
            ApprovalRequestRepo approvalRequestRepo,
            ApprovalWorkflowRepo approvalWorkflowRepo,
            ApprovalStepRepo approvalStepRepo) {
        this.approvalHistoryRepo = approvalHistoryRepo;
        this.approvalRequestRepo = approvalRequestRepo;
        this.approvalWorkflowRepo = approvalWorkflowRepo;
        this.approvalStepRepo = approvalStepRepo;
    }

    @ConsumeEvent
    public void approvalLogEvent(ApprovalHistory history) {
        approvalHistoryRepo.persist(history);
    }

    @Transactional
    public void submit(SecurityIdentity identity, String referenceId) {
        // check master approval by request type
        ApprovalWorkflow approvalWorkflow = approvalWorkflowRepo.findByRequestType(ApprovalType.REQUISITION);
        if (approvalWorkflow == null) {
            throw new RuntimeException("Approval workflow not found");
        }
        var stepApproval = approvalStepRepo.findByWorkflowIdAndStepOrder(approvalWorkflow.id, 1);
        if (stepApproval == null) {
            throw new RuntimeException("Approval step not found");
        }

        var newApprovalRequest = new ApprovalRequest();
        newApprovalRequest.referenceId = referenceId;
        newApprovalRequest.step = stepApproval;
        newApprovalRequest.workflow = approvalWorkflow;
        newApprovalRequest.status = ApprovalStatus.SUBMIT;
        newApprovalRequest.submittedBy = identity.getPrincipal().getName();

        var history = new ApprovalHistory();
        history.action = ApprovalStatus.SUBMIT;
        history.actedBy = identity.getPrincipal().getName();
        history.comment = "";
        history.step = stepApproval;
        history.request = newApprovalRequest;  // set back-reference for FK

        newApprovalRequest.histories.add(history);
        approvalRequestRepo.persist(newApprovalRequest);
    }

    @Transactional
    public void approve(SecurityIdentity identity, String referenceId) {
        // check master approval by request type
        ApprovalWorkflow approvalWorkflow = approvalWorkflowRepo.findByRequestType(ApprovalType.REQUISITION);
        if (approvalWorkflow == null) {
            throw new RuntimeException("Approval workflow not found");
        }

        var approvalRequest = approvalRequestRepo
                .find("referenceId = ?1 and status = ?2", referenceId, ApprovalStatus.SUBMIT)
                .firstResult();

        // check step approval has more or not
        var stepApproval = approvalStepRepo.findByWorkflowIdAndStepOrder(approvalRequest.step.workflow.id,
                approvalRequest.step.stepOrder + 1);

        if (stepApproval != null) {
            // check current user role
            if (identity.getRoles().stream().anyMatch(role -> role.contains(stepApproval.roleRequired.toLowerCase()))) {
                approvalRequest.status = ApprovalStatus.APPROVED;
                approvalRequest.step = stepApproval;
            } else {
                throw new RuntimeException("User is not authorized to approve this request");
            }
            var history = new ApprovalHistory();
            history.action = ApprovalStatus.APPROVED;
            history.actedBy = identity.getPrincipal().getName();
            history.comment = "";
            history.step = stepApproval;
            history.request = approvalRequest;  // set back-reference for FK

            approvalRequest.histories.add(history);
            approvalRequestRepo.persist(approvalRequest);
        } else {
            approvalRequest.status = ApprovalStatus.COMPLETED;
            var history = new ApprovalHistory();
            history.action = ApprovalStatus.COMPLETED;
            history.actedBy = identity.getPrincipal().getName();
            history.comment = "";
            history.step = approvalRequest.step;
            history.request = approvalRequest;  // set back-reference for FK

            approvalRequest.histories.add(history);
            approvalRequestRepo.persist(approvalRequest);
        }
    }

    @Transactional
    public void reject(SecurityIdentity identity, String referenceId) {
        // check master approval by request type
        ApprovalWorkflow approvalWorkflow = approvalWorkflowRepo.findByRequestType(ApprovalType.REQUISITION);
        if (approvalWorkflow == null) {
            throw new RuntimeException("Approval workflow not found");
        }

        var approvalRequest = approvalRequestRepo
                .find("referenceId = ?1 and status = ?2", referenceId, ApprovalStatus.SUBMIT)
                .firstResult();

        // check step approval has more or not
        var stepApproval = approvalStepRepo.findByWorkflowIdAndStepOrder(approvalRequest.step.workflow.id,
                approvalRequest.step.stepOrder + 1);
        if (stepApproval != null) {
            // check current user role
            if (identity.getRoles().stream().anyMatch(role -> role.contains(stepApproval.roleRequired))) {
                approvalRequest.status = ApprovalStatus.REJECTED;
            } else {
                throw new RuntimeException("User is not authorized to approve this request");
            }
            var history = new ApprovalHistory();
            history.action = ApprovalStatus.REJECTED;
            history.actedBy = identity.getPrincipal().getName();
            history.comment = "";
            history.step = stepApproval;
            history.request = approvalRequest;  // set back-reference for FK

            approvalRequest.histories.add(history);
            approvalRequestRepo.persist(approvalRequest);
        } else {
            approvalRequest.status = ApprovalStatus.REJECTED;
            var history = new ApprovalHistory();
            history.action = ApprovalStatus.REJECTED;
            history.actedBy = identity.getPrincipal().getName();
            history.comment = "";
            history.step = approvalRequest.step;
            history.request = approvalRequest;  // set back-reference for FK

            approvalRequest.histories.add(history);
            approvalRequestRepo.persist(approvalRequest);
        }
    }
}
