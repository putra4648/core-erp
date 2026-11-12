package id.putra.core_erp.modules.procurement.service;

import java.math.BigDecimal;
import java.util.List;
import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.enums.ApprovalType;
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
		// Validate budget availability before entering the approval workflow.
		// The actual deduction happens only when the final approval step completes,
		// but this pre-check prevents obviously unaffordable requisitions from
		// being submitted for approval at all.
		if (entity.budget == null) {
			throw new RuntimeException("Budget is required");
		}
		BigDecimal totalReq = calculateTotal(entity);
		if (entity.budget.allocated == null
				|| entity.budget.allocated.subtract(totalReq).compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("Budget not available: required " + totalReq.toPlainString()
					+ " but only "
					+ (entity.budget.allocated != null ? entity.budget.allocated.toPlainString() : "0")
					+ " remaining in budget '" + entity.budget.name + "'");
		}

		approvalService.submit(identity, ApprovalType.REQUISITION, entity.referenceId);

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

		// Advance the approval workflow first. The budget is only deducted when
		// the final approval step is completed (status == COMPLETED), so that
		// intermediate approvals do not cut the budget multiple times.
		var approvalStatus = approvalService.approve(identity, ApprovalType.REQUISITION, requisition.referenceId, null);
		if (approvalStatus == ApprovalStatus.COMPLETED) {
			var budget = requisition.budget;
			if (budget == null) {
				throw new RuntimeException("Budget not found");
			}
			BigDecimal totalReq = calculateTotal(requisition);
			if (budget.allocated.subtract(totalReq).compareTo(BigDecimal.ZERO) < 0) {
				throw new RuntimeException("Budget not available");
			}
			budget.allocated = budget.allocated.subtract(totalReq);
			budgetRepo.getEntityManager().merge(budget);

			requisition.status = ApprovalStatus.APPROVED;
		}
		requisitionRepo.persist(requisition);
	}

	public void reject(Long id, SecurityIdentity identity, String comment) {
		var requisition = requisitionRepo.findById(id);
		if (requisition == null) {
			throw new RuntimeException("Requisition not found");
		}
		if (requisition.status != ApprovalStatus.SUBMIT) {
			throw new RuntimeException("Requisition is not in submit status");
		}
		approvalService.reject(identity, ApprovalType.REQUISITION, requisition.referenceId, comment);
		requisition.status = ApprovalStatus.REJECTED;
		requisitionRepo.persist(requisition);
	}

	@Override
	public void update(Requisition entity) {
		throw new UnsupportedOperationException("Not using");
	}

	@Override
	public void delete(Long id) {
		requisitionRepo.softDeleteById(id);
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

	/**
	 * Calculates the total monetary value of a requisition by summing
	 * {@code unitPrice * quantity} across all line items. Used by both
	 * {@link #submit} (pre-check) and {@link #approve} (final deduction) so the
	 * total is always computed consistently.
	 *
	 * @param requisition the requisition to total
	 * @return the total amount (never {@code null}; zero if no items)
	 */
	private BigDecimal calculateTotal(Requisition requisition) {
		BigDecimal total = BigDecimal.ZERO;
		if (requisition.items != null) {
			for (var item : requisition.items) {
				if (item.unitPrice != null && item.quantity != null) {
					total = total.add(item.unitPrice.multiply(BigDecimal.valueOf(item.quantity)));
				}
			}
		}
		return total;
	}

	@Override
	public long count() {
		return requisitionRepo.count();
	}
}
