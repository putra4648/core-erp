package id.putra.core_erp.modules.inbound.model;

import java.time.LocalDateTime;
import java.util.List;
import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.OrderStatus;
import id.putra.core_erp.modules.procurement.model.PurchaseOrder;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class Receiving extends BaseEntity {

	public LocalDateTime receivedDate;

	/**
	 * Inbound receipt tracking number (e.g. {@code REC-2025-001}). Auto-generated
	 * when a goods receipt is created from a Purchase Order as
	 * {@code "REC-" + poNumber}.
	 */
	public String receivingNumber;

	@Enumerated(EnumType.STRING)
	public OrderStatus status;

	@OneToMany(mappedBy = "receiving", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ReceivingLine> receivingLines;

	@ManyToOne
	@JoinColumn(name = "purchase_order_id")
	public PurchaseOrder purchaseOrder;

}
