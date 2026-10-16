package id.putra.core_erp.modules.procurement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.modules.supplier.model.Supplier;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Quotation extends BaseEntity {

	@Column(name = "rfq_number")
	public String rfqNumber;

	@Column(name = "due_date")
	public LocalDate dueDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	public ApprovalStatus status;

	@Column(name = "note")
	public String note;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "suppliers", columnDefinition = "jsonb")
	public List<Supplier> suppliers = new ArrayList<>();

	/**
	 * The approved requisition (PR) that triggered this Request for Quotation.
	 * Establishes the {@code Requisition -> Quotation} link in the P2P flow.
	 */
	@ManyToOne
	@JoinColumn(name = "requisition_id")
	public Requisition requisition;

	/**
	 * The winning supplier selected by the procurement officer after comparing
	 * the candidate {@link #suppliers} price quotes.
	 */
	@ManyToOne
	@JoinColumn(name = "selected_supplier_id")
	public Supplier selectedSupplier;

	/**
	 * The line items being quoted (product, quantity, unit price). These lines
	 * are copied into {@link PurchaseOrderLine}s when a PO is generated.
	 */
	@OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	public List<QuotationLine> quotationLines = new ArrayList<>();

	/**
	 * The purchase order generated from this quotation once it is approved and
	 * a winning supplier has been selected. Establishes the
	 * {@code Quotation -> PurchaseOrder} link in the P2P flow.
	 */
	@OneToOne
	@JoinColumn(name = "purchase_order_id")
	public PurchaseOrder purchaseOrder;

}
