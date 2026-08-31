package id.putra.core_erp.purchase_requisition.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BudgetDto extends BaseModelDto {
    private String id;
    private DepartmentDto department;
    private String fy;
    private List<BudgetDetailDto> details;
}
