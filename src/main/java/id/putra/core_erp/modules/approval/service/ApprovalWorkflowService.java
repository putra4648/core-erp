package id.putra.core_erp.modules.approval.service;

import java.util.List;

import id.putra.core_erp.modules.approval.model.ApprovalWorkflow;
import id.putra.core_erp.modules.approval.repository.ApprovalWorkflowRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ApprovalWorkflowService implements BaseService<ApprovalWorkflow> {

    private final ApprovalWorkflowRepo approvalWorkflowRepo;

    public ApprovalWorkflowService(ApprovalWorkflowRepo approvalWorkflowRepo) {
        this.approvalWorkflowRepo = approvalWorkflowRepo;
    }

    @Override
    public void save(ApprovalWorkflow entity) {
        approvalWorkflowRepo.persist(entity);
    }

    @Override
    public ApprovalWorkflow findById(Long id) {
        return approvalWorkflowRepo.findById(id);
    }

    @Override
    public void delete(Long id) {
        approvalWorkflowRepo.deleteById(id);
    }

    @Override
    public void update(ApprovalWorkflow entity) {
        approvalWorkflowRepo.getEntityManager().merge(entity);
    }

    @Override
    public List<ApprovalWorkflow> findAll(ApprovalWorkflow entity, Page page) {
        return approvalWorkflowRepo.findAll().page(page).list();
    }

}
