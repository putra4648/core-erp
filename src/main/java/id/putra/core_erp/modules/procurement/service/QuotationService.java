package id.putra.core_erp.modules.procurement.service;

import java.util.List;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.modules.approval.service.ApprovalService;
import id.putra.core_erp.modules.inbound.service.PurchaseOrderService;
import id.putra.core_erp.modules.procurement.model.Quotation;
import id.putra.core_erp.modules.procurement.repository.QuotationRepo;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import id.putra.core_erp.modules.procurement.repository.SupplierRepo;
import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class QuotationService implements BaseService<Quotation> {

	private final QuotationRepo quotationRepository;
	private final SupplierRepo supplierRepo;
	private final RequisitionRepo requisitionRepo;
	private final ApprovalService approvalService;
	private final PurchaseOrderService purchaseOrderService;

	public QuotationService(QuotationRepo rfqRepository, SupplierRepo supplierRepo, RequisitionRepo requisitionRepo,
			ApprovalService approvalService, PurchaseOrderService purchaseOrderService) {
		this.quotationRepository = rfqRepository;
		this.supplierRepo = supplierRepo;
		this.requisitionRepo = requisitionRepo;
		this.approvalService = approvalService;
		this.purchaseOrderService = purchaseOrderService;
	}

	@PersistenceContext
	private EntityManager entityManager;

	public void add(Quotation dto) {
		if (dto.status == null) {
			dto.status = ApprovalStatus.SUBMIT;
		}
		quotationRepository.persist(dto);
	}

	/**
	 * Submits a Request for Quotation into the approval workflow (P2P step:
	 * "Submit Quotation Approval"). The quotation must reference an approved
	 * requisition before it can be routed for approval.
	 */
	public void submit(Quotation entity, SecurityIdentity identity) {
		if (entity.requisition == null || entity.requisition.id == null) {
			throw new RuntimeException("Quotation must reference an approved requisition");
		}
		var requisition = requisitionRepo.findById(entity.requisition.id);
		if (requisition == null) {
			throw new RuntimeException("Source requisition not found");
		}
		if (requisition.status != ApprovalStatus.APPROVED) {
			throw new RuntimeException("Source requisition must be APPROVED before issuing an RFQ");
		}
		entity.requisition = requisition;
		entity.status = ApprovalStatus.SUBMIT;
		quotationRepository.persist(entity);

		approvalService.submit(identity, ApprovalType.QUOTATION, entity.rfqNumber != null ? entity.rfqNumber
				: ("RFQ-" + entity.id));
	}

	/**
	 * Selects the winning supplier for a quotation after comparing candidate
	 * price quotes (P2P step: "Select Winning Supplier"). The selected supplier
	 * is recorded so that a Purchase Order can subsequently be generated.
	 */
	public void selectWinner(Long quotationId, Long supplierId) {
		var quotation = quotationRepository.findById(quotationId);
		if (quotation == null) {
			throw new RuntimeException("Quotation not found");
		}
		Supplier supplier = supplierRepo.findById(supplierId);
		if (supplier == null) {
			throw new RuntimeException("Supplier not found");
		}
		quotation.selectedSupplier = supplier;
		quotationRepository.getEntityManager().merge(quotation);
	}

	/**
	 * Approves a submitted quotation through the configured approval workflow.
	 * Once all approval steps are completed the quotation status is set to
	 * {@link ApprovalStatus#APPROVED}, allowing a Purchase Order to be generated.
	 */
	public void approve(Long id, SecurityIdentity identity) {
		var quotation = quotationRepository.findById(id);
		if (quotation == null) {
			throw new RuntimeException("Quotation not found");
		}
		if (quotation.status != ApprovalStatus.SUBMIT) {
			throw new RuntimeException("Quotation is not in submit status");
		}
		if (quotation.selectedSupplier == null) {
			throw new RuntimeException("Cannot approve quotation: no winning supplier selected");
		}
		String referenceId = quotation.rfqNumber != null ? quotation.rfqNumber : ("RFQ-" + quotation.id);
		var approvalStatus = approvalService.approve(identity, ApprovalType.QUOTATION, referenceId, null);
		if (approvalStatus == ApprovalStatus.COMPLETED) {
			quotation.status = ApprovalStatus.APPROVED;
		}
		quotationRepository.getEntityManager().merge(quotation);
	}

	/**
	 * Generates a Purchase Order from an approved quotation (P2P step:
	 * "Generate Purchase Order").
	 * <p>
	 * Delegates to {@link PurchaseOrderService#generateFromQuotation} to create
	 * the PO and copy its lines, links the quotation to the generated PO, and
	 * finally advances the source requisition status to
	 * {@link ApprovalStatus#COMPLETED} (the requisition's lifecycle is complete
	 * once its PO has been created).
	 *
	 * @return the generated purchase order
	 */
	public id.putra.core_erp.modules.procurement.model.PurchaseOrder generatePurchaseOrder(Long quotationId) {
		var quotation = quotationRepository.findById(quotationId);
		if (quotation == null) {
			throw new RuntimeException("Quotation not found");
		}
		if (quotation.status != ApprovalStatus.APPROVED) {
			throw new RuntimeException("Quotation must be APPROVED before generating a Purchase Order");
		}
		if (quotation.selectedSupplier == null) {
			throw new RuntimeException("Quotation has no winning supplier selected");
		}
		if (quotation.purchaseOrder != null) {
			throw new RuntimeException("A Purchase Order has already been generated for this quotation");
		}

		var po = purchaseOrderService.generateFromQuotation(quotation);

		// persist the quotation side of the Quotation <-> PurchaseOrder link
		quotationRepository.getEntityManager().merge(quotation);

		// advance the source requisition lifecycle now that a PO exists
		if (quotation.requisition != null) {
			var requisition = requisitionRepo.findById(quotation.requisition.id);
			if (requisition != null) {
				requisition.status = ApprovalStatus.COMPLETED;
				requisitionRepo.getEntityManager().merge(requisition);
			}
		}

		return po;
	}

	public List<Quotation> findAll(Quotation params) {
		if (params.rfqNumber != null && !params.rfqNumber.isBlank()) {
			return quotationRepository.find("LOWER(rfqNumber) like LOWER(concat('%', ?1, '%'))", params.rfqNumber)
					.list();
		}
		if (params.id != null) {
			return quotationRepository.find("CAST(id AS text) like concat('%', ?1, '%')", params.id.toString())
					.list();
		}
		return quotationRepository.findAll().list();
	}

	@Override
	public void save(Quotation entity) {
		quotationRepository.persist(entity);
	}

	@Override
	public Quotation findById(Long id) {
		return quotationRepository.findById(id);
	}

	@Override
	public List<Quotation> findAll(Quotation quotation, Page p) {
		return quotationRepository.findAll().list();
	}

	public Quotation getById(Long id) {
		return quotationRepository.findByIdOptional(id).get();
	}

	@Override
	public void update(Quotation entity) {
		quotationRepository.getEntityManager().merge(entity);
	}

	@Override
	public void delete(Long id) {
		quotationRepository.softDeleteById(id);
	}

}
