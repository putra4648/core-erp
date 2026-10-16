package id.putra.core_erp.modules.procurement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.OrderStatus;
import id.putra.core_erp.modules.supplier.model.Supplier;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table
public class PurchaseOrder extends BaseEntity {

	@NotBlank(message = "PO Number is required")
	public String poNumber;

	@Enumerated(EnumType.STRING)
	public OrderStatus status;

	public LocalDateTime orderDate;

	/**
	 * Expected delivery date agreed with the vendor. Populated when the PO is
	 * generated from an approved quotation.
	 */
	public LocalDate deliveryDate;

	@OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PurchaseOrderLine> purchaseOrderLines = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "supplier_id")
	public Supplier supplier;

	/**
	 * The approved quotation (RFQ) this purchase order was generated from.
	 * Inverse side of the {@code Quotation.purchaseOrder} relationship, giving
	 * full P2P traceability from RFQ to PO.
	 */
	@OneToOne(mappedBy = "purchaseOrder")
	public Quotation quotation;

}
