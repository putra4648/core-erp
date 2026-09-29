package id.putra.core_erp.modules.transaction.model;

import java.math.BigDecimal;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.inventory.model.InventoryItem;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction_items")
public class TransactionItem extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "dest_inv_item_id")
	private InventoryItem destInventoryItem;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String lotNumber;

	private String notes;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	private Long quantity;
	private String serialNumber;
	@ManyToOne
	@JoinColumn(name = "source_inv_item_id")
	private InventoryItem sourceInventoryItem;
	private BigDecimal totalPrice;
	@ManyToOne
	@JoinColumn(name = "transaction_id", nullable = false)
	private Transaction transaction;
	private BigDecimal unitPrice;

	public InventoryItem getDestInventoryItem() {
		return destInventoryItem;
	}

	public Long getId() {
		return id;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public String getNotes() {
		return notes;
	}

	public Product getProduct() {
		return product;
	}

	public Long getQuantity() {
		return quantity;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public InventoryItem getSourceInventoryItem() {
		return sourceInventoryItem;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setDestInventoryItem(InventoryItem destInventoryItem) {
		this.destInventoryItem = destInventoryItem;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setSourceInventoryItem(InventoryItem sourceInventoryItem) {
		this.sourceInventoryItem = sourceInventoryItem;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
}
