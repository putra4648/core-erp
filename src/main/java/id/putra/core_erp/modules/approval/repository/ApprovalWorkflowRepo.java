package id.putra.core_erp.modules.approval.repository;

import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.modules.approval.model.ApprovalWorkflow;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ApprovalWorkflowRepo implements SoftDeleteRepository<ApprovalWorkflow> {

    @Inject
    ApprovalStepRepo approvalStepRepo;

    /**
     * Steps belong to their workflow. Runtime requests/histories are deliberately left
     * alone: they are the approval audit trail, and {@code ApprovalRequestRepo} /
     * {@code ApprovalHistoryRepo} still hard-delete by design.
     */
    @Override
    public void cascadeToChildren(ApprovalWorkflow workflow) {
        approvalStepRepo.find("workflow.id", workflow.id).list()
                .forEach(approvalStepRepo::softDelete);
    }

    public ApprovalWorkflow findByRequestType(ApprovalType type) {
        return find("requestType", type).firstResult();
    }

}
