package id.putra.core_erp.modules.transaction.model;

import java.math.BigDecimal;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.inventory.model.InventoryItem;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class TransactionItem extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "transaction_id", nullable = false)
	public Transaction transaction;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	public Product product;

	@ManyToOne
	@JoinColumn(name = "source_inv_item_id")
	public InventoryItem sourceInventoryItem;

	@ManyToOne
	@JoinColumn(name = "dest_inv_item_id")
	public InventoryItem destInventoryItem;

	public Integer quantity;
	public BigDecimal unitPrice;
	public BigDecimal totalPrice;
	public String lotNumber;
	public String serialNumber;
	public String notes;

}
