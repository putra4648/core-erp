package id.putra.core_erp.warehouse.module.transaction.dto;

import java.math.BigDecimal;

import id.putra.core_erp.shared.dto.BaseDto;

public class TransactionItemDto extends BaseDto {
    private Long id;

    private Long transactionId;
    private Long productId;
    private Long sourceInventoryItemId;
    private Long destInventoryItemId;

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

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSourceInventoryItemId() {
        return sourceInventoryItemId;
    }

    public void setSourceInventoryItemId(Long sourceInventoryItemId) {
        this.sourceInventoryItemId = sourceInventoryItemId;
    }

    public Long getDestInventoryItemId() {
        return destInventoryItemId;
    }

    public void setDestInventoryItemId(Long destInventoryItemId) {
        this.destInventoryItemId = destInventoryItemId;
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
