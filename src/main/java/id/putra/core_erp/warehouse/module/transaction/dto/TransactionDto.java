package id.putra.core_erp.warehouse.module.transaction.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import id.putra.core_erp.shared.dto.BaseDto;

public class TransactionDto extends BaseDto {
    private Long id;

    private String txnNumber;
    private String txnType;
    private ZonedDateTime txnDate;
    private String sourceLocId;
    private String destLocId;
    private String refNumber;
    private String refType;
    private String status;
    private String notes;
    private BigDecimal totalAmount;
    private Long quantity;

    private Long supplierId;
    private Long customerId;
    private List<TransactionItemDto> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxnNumber() {
        return txnNumber;
    }

    public void setTxnNumber(String txnNumber) {
        this.txnNumber = txnNumber;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public ZonedDateTime getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(ZonedDateTime txnDate) {
        this.txnDate = txnDate;
    }

    public String getSourceLocId() {
        return sourceLocId;
    }

    public void setSourceLocId(String sourceLocId) {
        this.sourceLocId = sourceLocId;
    }

    public String getDestLocId() {
        return destLocId;
    }

    public void setDestLocId(String destLocId) {
        this.destLocId = destLocId;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<TransactionItemDto> getItems() {
        return items;
    }

    public void setItems(List<TransactionItemDto> items) {
        this.items = items;
    }

}
