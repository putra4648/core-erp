package id.putra.core_erp.warehouse.module.inbound.dto;

import java.sql.Date;
import java.util.List;

import id.putra.core_erp.shared.enums.OrderStatus;

public class PurchaseOrderDto {
    private Long id;
    private String poNumber;
    private OrderStatus status;
    private Date orderDate;
    private Long supplierId;
    private List<PurchaseOrderLineDto> purchaseOrderLines;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public List<PurchaseOrderLineDto> getPurchaseOrderLines() {
        return purchaseOrderLines;
    }

    public void setPurchaseOrderLines(List<PurchaseOrderLineDto> purchaseOrderLines) {
        this.purchaseOrderLines = purchaseOrderLines;
    }

}
