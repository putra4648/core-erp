package id.putra.core_erp.purchaserequisition.dto;

import java.util.List;
import id.putra.core_erp.purchaserequisition.enums.BudgetEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BudgetDetailDto {
    private String name;
    private String budget_no;
    private String note;
    private BudgetEnum type;
    private List<BudgetTypeDto> plans;
    private String total_budget;
}

