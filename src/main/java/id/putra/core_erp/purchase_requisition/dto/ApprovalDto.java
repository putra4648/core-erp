package id.putra.core_erp.purchase_requisition.dto;

public class ApprovalDto {
    private Integer id;
    private UsersDto users;
    private Boolean isApprovalChecked;
    private Boolean isBypassChecked;
    private String bypassReason;

    // Additional
    private Boolean isApprovalPermanent;
    private Boolean isBypassPermanent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UsersDto getUsers() {
        return users;
    }

    public void setUsers(UsersDto users) {
        this.users = users;
    }

    public Boolean getIsApprovalChecked() {
        return isApprovalChecked;
    }

    public void setIsApprovalChecked(Boolean isApprovalChecked) {
        this.isApprovalChecked = isApprovalChecked;
    }

    public Boolean getIsBypassChecked() {
        return isBypassChecked;
    }

    public void setIsBypassChecked(Boolean isBypassChecked) {
        this.isBypassChecked = isBypassChecked;
    }

    public String getBypassReason() {
        return bypassReason;
    }

    public void setBypassReason(String bypassReason) {
        this.bypassReason = bypassReason;
    }

    public Boolean getIsApprovalPermanent() {
        return isApprovalPermanent;
    }

    public void setIsApprovalPermanent(Boolean isApprovalPermanent) {
        this.isApprovalPermanent = isApprovalPermanent;
    }

    public Boolean getIsBypassPermanent() {
        return isBypassPermanent;
    }

    public void setIsBypassPermanent(Boolean isBypassPermanent) {
        this.isBypassPermanent = isBypassPermanent;
    }
}
