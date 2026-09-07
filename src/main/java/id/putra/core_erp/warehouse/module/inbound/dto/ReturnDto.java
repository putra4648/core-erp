package id.putra.core_erp.warehouse.module.inbound.dto;

public class ReturnDto {
    private Long id;
    private String reason;
    private String status;
    private Long receivingLineId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
}
