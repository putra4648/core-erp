package id.putra.core_erp.modules.inbound.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.procurement.model.PurchaseOrderLine;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class ReceivingLine extends BaseEntity {

	public LocalDateTime receivedDate;
	public String status;
	public String lotBatch;
	public LocalDateTime expiryDate;

	@Column(precision = 12, scale = 2)
	public BigDecimal qtyReceived;

	@Column(precision = 12, scale = 2, name = "ordered_qty")
	public BigDecimal qty;

	@ManyToOne
	@JoinColumn(name = "receiving_id")
	public Receiving receiving;

	@ManyToOne
	@JoinColumn(name = "product_id")
	public Product product;

	/**
	 * The Purchase Order line this receiving line fulfills, providing full P2P
	 * traceability from Purchase Order to Goods Receipt. Populated when the
	 * receiving is generated from a Purchase Order.
	 */
	@ManyToOne
	@JoinColumn(name = "purchase_order_line_id")
	public PurchaseOrderLine purchaseOrderLine;

}
