package id.putra.core_erp.modules.approval.repository;

import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.modules.approval.model.ApprovalStep;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApprovalStepRepo implements PanacheRepository<ApprovalStep> {
    public ApprovalStep findByWorkflowIdAndStepOrder(Long workflowId, Integer stepOrder) {
        return find("workflow.id = ?1 and stepOrder = ?2", workflowId, stepOrder).firstResult();
    }

}
