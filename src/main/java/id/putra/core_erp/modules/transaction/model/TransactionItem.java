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
<<<<<<< HEAD
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
=======
	@JoinColumn(name = "transaction_id", nullable = false)
	private Transaction transaction;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@ManyToOne
	@JoinColumn(name = "source_inv_item_id")
	private InventoryItem sourceInventoryItem;

	@ManyToOne
	@JoinColumn(name = "dest_inv_item_id")
	private InventoryItem destInventoryItem;

	private Long quantity;
	private BigDecimal unitPrice;
	private BigDecimal totalPrice;
	private String lotNumber;
	private String serialNumber;
	private String notes;

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public InventoryItem getSourceInventoryItem() {
		return sourceInventoryItem;
	}

	public void setSourceInventoryItem(InventoryItem sourceInventoryItem) {
		this.sourceInventoryItem = sourceInventoryItem;
	}

	public InventoryItem getDestInventoryItem() {
		return destInventoryItem;
	}

	public void setDestInventoryItem(InventoryItem destInventoryItem) {
		this.destInventoryItem = destInventoryItem;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}
}
