package id.putra.core_erp.modules.budget.model;

import java.util.Date;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.department.model.Department;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "budget")
public class Budget extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "department", referencedColumnName = "dept_number")
    private Department department;

    @Column(name = "fy")
    private Date fiscalYear;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetDetail> details;


    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Date getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Date fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public List<BudgetDetail> getDetails() {
        return details;
    }

    public void setDetails(List<BudgetDetail> details) {
        this.details = details;
    }
}
