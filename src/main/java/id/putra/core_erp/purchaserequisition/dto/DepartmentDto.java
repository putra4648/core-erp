package id.putra.core_erp.purchaserequisition.dto;

import java.util.List;
import id.putra.core_erp.purchaserequisition.enums.StatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DepartmentDto {
    private String dept_number;
    private String dept_name;
    private StatusEnum status;
    private UsersDto dept_manager;
    private List<ApprovalDto> approvals;
    private UsersDto dept_purchaser;
    private UsersDto approval_president;
}

