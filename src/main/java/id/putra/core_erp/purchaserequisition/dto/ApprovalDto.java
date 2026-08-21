package id.putra.core_erp.purchaserequisition.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ApprovalDto {
    private Integer id;
    private UsersDto users;
    private Boolean isApprovalChecked;
    private Boolean isBypassChecked;
    private String bypassReason;

    // Additional
    private Boolean isApprovalPermanent;
    private Boolean isBypassPermanent;
}

