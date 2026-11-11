package id.putra.core_erp.modules.approval.repository;

import id.putra.core_erp.modules.approval.model.ApprovalStep;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApprovalStepRepo implements SoftDeleteRepository<ApprovalStep> {
    public ApprovalStep findByWorkflowIdAndStepOrder(Long workflowId, Integer stepOrder) {
        return find("workflow.id = ?1 and stepOrder = ?2", workflowId, stepOrder).firstResult();
    }

    public ApprovalStep findFirstByWorkflowId(Long workflowId) {
        return find("workflow.id = ?1 order by stepOrder asc", workflowId).firstResult();
    }

}
