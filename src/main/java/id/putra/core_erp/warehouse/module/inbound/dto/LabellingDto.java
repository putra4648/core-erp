package id.putra.core_erp.warehouse.module.inbound.dto;


public class LabellingDto {
    private Long id;
    private String labelContent;
    private String status;
    private Long receivingLineId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabelContent() {
        return labelContent;
    }

    public void setLabelContent(String labelContent) {
        this.labelContent = labelContent;
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

