package id.putra.core_erp.modules.transaction.model;

import java.math.BigDecimal;

import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.inventory.model.InventoryItem;
import id.putra.core_erp.shared.entity.BaseEntity;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    }
}
