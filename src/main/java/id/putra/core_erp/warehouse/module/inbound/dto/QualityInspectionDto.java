package id.putra.core_erp.warehouse.module.inbound.dto;

public class QualityInspectionDto {
    private Long id;
    private String comments;
    private String status;
    private Long receivingLineId;
    private Long inspectorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public Long getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(Long inspectorId) {
        this.inspectorId = inspectorId;
    }

}
