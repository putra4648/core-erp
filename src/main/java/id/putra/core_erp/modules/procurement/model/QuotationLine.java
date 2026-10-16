package id.putra.core_erp.modules.procurement.model;

import java.math.BigDecimal;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * A line item on a {@link Quotation} (RFQ) describing the product, quantity
 * and quoted unit price solicited from candidate suppliers. These lines are the
 * source from which {@link PurchaseOrderLine}s are generated when a Purchase
 * Order is created from an approved quotation.
 */
@Entity
@Table
public class QuotationLine extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "product_id")
	public Product product;

	@NotNull(message = "Quantity is required")
	public Integer quantity;

	@NotNull(message = "Unit price is required")
	public BigDecimal unitPrice;

	@ManyToOne
	@JoinColumn(name = "quotation_id")
	public Quotation quotation;

}