package id.putra.core_erp.modules.procurement.model;

import java.math.BigDecimal;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class PurchaseOrderLine extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	private Integer quantity;
	private BigDecimal price;

	/**
	 * Cumulative quantity received against this PO line across one or more goods
	 * receipts. Used to track partial deliveries and determine when the PO line
	 * is fully fulfilled (qtyReceived &gt;= quantity).
	 */
	private Integer qtyReceived = 0;

	@ManyToOne
	@JoinColumn(name = "purchase_order_id")
	private PurchaseOrder purchaseOrder;

	/**
	 * The quotation line this PO line was generated from, providing full P2P
	 * traceability from RFQ to Purchase Order.
	 */
	@ManyToOne
	@JoinColumn(name = "quotation_line_id")
	private QuotationLine quotationLine;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return cumulative quantity received against this PO line (defaults to 0).
	 */
	public Integer getQtyReceived() {
		return qtyReceived;
	}

	public void setQtyReceived(Integer qtyReceived) {
		this.qtyReceived = qtyReceived;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public QuotationLine getQuotationLine() {
		return quotationLine;
	}

	public void setQuotationLine(QuotationLine quotationLine) {
		this.quotationLine = quotationLine;
	}
}
