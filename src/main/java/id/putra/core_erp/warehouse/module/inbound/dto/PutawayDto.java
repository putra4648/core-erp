package id.putra.core_erp.warehouse.module.inbound.dto;


public class PutawayDto {
    private Long id;
    private String status;
    private Long receivingLineId;
    private Long locationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getReceivingLineId() {
        return receivingLineId;
    }

    public void setReceivingLineId(Long receivingLineId) {
        this.receivingLineId = receivingLineId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

}

