package id.putra.core_erp.modules.approval.service;

import java.util.List;

import id.putra.core_erp.enums.ApprovalType;
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
        approvalWorkflowRepo.softDeleteById(id);
    }

    @Override
    public void update(ApprovalWorkflow entity) {
        approvalWorkflowRepo.getEntityManager().merge(entity);
    }

    @Override
    public List<ApprovalWorkflow> findAll(ApprovalWorkflow entity, Page page) {
        return approvalWorkflowRepo.findAll().page(page).list();
    }

    public ApprovalWorkflow findByRequestType(ApprovalType type) {
        if (type == null) {
            throw new RuntimeException("Type cannot be null");
        }

        return approvalWorkflowRepo.find("requestType", type).firstResult();
    }

    /**
     * Returns the list of role names that are part of the approval workflow for the
     * given request type. The {@code steps} collection is accessed within this
     * transactional method so that it is initialized before the entity becomes
     * detached, avoiding {@code LazyInitializationException} in the caller.
     */
    public List<String> findApproverRoles(ApprovalType type) {
        if (type == null) {
            return List.of();
        }
        ApprovalWorkflow workflow = approvalWorkflowRepo.find("requestType", type).firstResult();
        if (workflow == null || workflow.steps == null) {
            return List.of();
        }
        return workflow.steps.stream().map(s -> s.roleRequired).toList();
    }

	@Override
	public long count() {
		return approvalWorkflowRepo.count();
	}
}
