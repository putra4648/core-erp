package id.putra.core_erp.modules.procurement.service;

import java.math.BigDecimal;
import java.util.List;
import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.modules.approval.service.ApprovalService;
import id.putra.core_erp.modules.budget.repository.BudgetRepo;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.model.RequisitionItem;
import id.putra.core_erp.modules.procurement.repository.RequisitionItemRepo;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class RequisitionService implements BaseService<Requisition> {

	private final ApprovalService approvalService;
	private final RequisitionRepo requisitionRepo;
	private final RequisitionItemRepo requisitionItemRepo;
	private final BudgetRepo budgetRepo;

	public RequisitionService(
			ApprovalService approvalService,
			RequisitionRepo requisitionRepo,
			RequisitionItemRepo requisitionItemRepo,
			BudgetRepo budgetRepo) {
		this.approvalService = approvalService;
		this.requisitionRepo = requisitionRepo;
		this.requisitionItemRepo = requisitionItemRepo;
		this.budgetRepo = budgetRepo;
	}

	@Override
	public void save(Requisition entity) {
		throw new UnsupportedOperationException("Please use saveWithApproval method instead");
	}

	public void submit(Requisition entity, SecurityIdentity identity) {

		// check budget available

		var budget = entity.budget;
		if (budget == null) {
			throw new RuntimeException("Budget not found");
		}
		BigDecimal totalReq = new BigDecimal(0);
		for (var item : entity.items) {
			totalReq = totalReq.add(item.unitPrice.multiply(BigDecimal.valueOf(item.quantity)));
		}
		if (budget.allocated.subtract(totalReq).compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("Budget not available");
		}
		budget.allocated = budget.allocated.subtract(totalReq);
		budgetRepo.getEntityManager().merge(budget);

		approvalService.submit(identity, entity.referenceId);

		requisitionRepo.persist(entity);
	}

	public void approve(Long id, SecurityIdentity identity) {
		var requisition = requisitionRepo.findById(id);
		if (requisition == null) {
			throw new RuntimeException("Requisition not found");
		}
		if (requisition.status != ApprovalStatus.SUBMIT) {
			throw new RuntimeException("Requisition is not in submit status");
		}
		approvalService.approve(identity, requisition.referenceId);
		requisition.status = ApprovalStatus.APPROVED;
		requisitionRepo.persist(requisition);
	}

	public void reject(Long id, SecurityIdentity identity) {
		var requisition = requisitionRepo.findById(id);
		if (requisition == null) {
			throw new RuntimeException("Requisition not found");
		}
		if (requisition.status != ApprovalStatus.SUBMIT) {
			throw new RuntimeException("Requisition is not in submit status");
		}
		approvalService.reject(identity, requisition.referenceId);
		requisition.status = ApprovalStatus.REJECTED;
		requisitionRepo.persist(requisition);
	}

	@Override
	public void update(Requisition entity) {
		throw new UnsupportedOperationException("Not using");
	}

	@Override
	public void delete(Long id) {
		requisitionRepo.deleteById(id);
	}

	@Override
	public Requisition findById(Long id) {
		return requisitionRepo.findById(id);
	}

	@Override
	public List<Requisition> findAll(Requisition r, Page p) {
		return requisitionRepo.findAll().page(p).list();
	}

	public List<RequisitionItem> findAllItem(Long id, Page p) {
		return requisitionItemRepo.findByRequisitionId(id, p);
	}

}
