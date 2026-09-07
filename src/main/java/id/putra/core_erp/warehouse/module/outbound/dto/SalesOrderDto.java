package id.putra.core_erp.warehouse.module.outbound.dto;

import java.sql.Date;
import java.util.List;

public class SalesOrderDto {
    private Long id;
    private String soNumber;
    private Date orderDate;
    private String status;
    private Long customerId;
    private List<SalesOrderLineDto> salesOrderLines;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSoNumber() {
        return soNumber;
    }

    public void setSoNumber(String soNumber) {
        this.soNumber = soNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<SalesOrderLineDto> getSalesOrderLines() {
        return salesOrderLines;
    }

    public void setSalesOrderLines(List<SalesOrderLineDto> salesOrderLines) {
        this.salesOrderLines = salesOrderLines;
    }

}
