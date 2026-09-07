package id.putra.core_erp.warehouse.module.inbound.dto;

import java.math.BigDecimal;
import java.sql.Date;

import id.putra.core_erp.shared.dto.ProductDto;
import id.putra.core_erp.shared.enums.ReceivingStatus;
import id.putra.core_erp.warehouse.module.warehouse.dto.LocationDto;

public class ReceivingLineDto {
    private Long id;
    private Date receivedDate;
    private ReceivingStatus status;
    private String lotBatch;
    private Date expiryDate;
    private BigDecimal qty;
    private BigDecimal qtyReceived;
    private ProductDto product;
    private LocationDto location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public ReceivingStatus getStatus() {
        return status;
    }

    public void setStatus(ReceivingStatus status) {
        this.status = status;
    }

    public String getLotBatch() {
        return lotBatch;
    }

    public void setLotBatch(String lotBatch) {
        this.lotBatch = lotBatch;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getQtyReceived() {
        return qtyReceived;
    }

    public void setQtyReceived(BigDecimal qtyReceived) {
        this.qtyReceived = qtyReceived;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

}
