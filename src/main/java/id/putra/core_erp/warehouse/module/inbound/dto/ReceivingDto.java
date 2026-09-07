package id.putra.core_erp.warehouse.module.inbound.dto;

import java.sql.Date;
import java.util.List;

public class ReceivingDto {
    private Long id;
    private String receivingNumber;
    private Date receivedDate;
    private String status;
    private PurchaseOrderDto purchaseOrder;
    private List<ReceivingLineDto> receivingLines;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceivingNumber() {
        return receivingNumber;
    }

    public void setReceivingNumber(String receivingNumber) {
        this.receivingNumber = receivingNumber;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PurchaseOrderDto getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrderDto purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public List<ReceivingLineDto> getReceivingLines() {
        return receivingLines;
    }

    public void setReceivingLines(List<ReceivingLineDto> receivingLines) {
        this.receivingLines = receivingLines;
    }

}
