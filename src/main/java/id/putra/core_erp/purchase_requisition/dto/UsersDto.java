package id.putra.core_erp.purchase_requisition.dto;

import java.util.Date;
import java.util.List;

import id.putra.core_erp.purchase_requisition.enums.RoleEnum;
import id.putra.core_erp.purchase_requisition.enums.StatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
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

    // private Boolean isApprovalChecked;
    // private Boolean isBypassChecked;
    // private String bypassReason;
}
