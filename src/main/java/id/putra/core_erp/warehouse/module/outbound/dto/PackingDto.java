package id.putra.core_erp.warehouse.module.outbound.dto;

import java.math.BigDecimal;
import java.util.List;

public class PackingDto {
    private Long id;
    private String packageNumber;
    private BigDecimal weight;
    private String dimension;
    private List<Long> shipmentIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(String packageNumber) {
        this.packageNumber = packageNumber;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public List<Long> getShipmentIds() {
        return shipmentIds;
    }

    public void setShipmentIds(List<Long> shipmentIds) {
        this.shipmentIds = shipmentIds;
    }

}
