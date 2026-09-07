package id.putra.core_erp.purchase_requisition.dto;

import java.util.List;

public class BudgetDto extends BaseModelDto {
    private String id;
    private DepartmentDto department;
    private String fy;
    private List<BudgetDetailDto> details;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DepartmentDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDto department) {
        this.department = department;
    }

    public String getFy() {
        return fy;
    }

    public void setFy(String fy) {
        this.fy = fy;
    }

    public List<BudgetDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<BudgetDetailDto> details) {
        this.details = details;
    }
}
