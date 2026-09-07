package id.putra.core_erp.purchase_requisition.dto;

import java.util.Date;
import java.util.List;

import id.putra.core_erp.purchase_requisition.enums.RoleEnum;
import id.putra.core_erp.purchase_requisition.enums.StatusEnum;

public class UsersDto {
    private String nrp;
    private String username;
    private String fullname;
    private String email;
    private GroupsDto groups;
    private String group_code;
    private StatusEnum status;
    private RoleEnum role;
    private Date createDate;
    private String profile_image;
    private List<DepartmentDto> departments;
    private String dept_id;
    private List<String> other_dept_ids;
    private String old_password;
    private String new_password;

    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GroupsDto getGroups() {
        return groups;
    }

    public void setGroups(GroupsDto groups) {
        this.groups = groups;
    }

    public String getGroup_code() {
        return group_code;
    }

    public void setGroup_code(String group_code) {
        this.group_code = group_code;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public List<DepartmentDto> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentDto> departments) {
        this.departments = departments;
    }

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public List<String> getOther_dept_ids() {
        return other_dept_ids;
    }

    public void setOther_dept_ids(List<String> other_dept_ids) {
        this.other_dept_ids = other_dept_ids;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    // private Boolean isApprovalChecked;
    // private Boolean isBypassChecked;
    // private String bypassReason;
}
