package id.putra.core_erp.purchase_requisition.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccessDto {
    private String accessNumber;
    private String accessItem;
    private String accessSupplier;
    private String accessBudget;
    private String accessRequisition;
    private String accessRfq;
    private String accessTrfRqst;
    private String accessInvtrCsmb;
}
