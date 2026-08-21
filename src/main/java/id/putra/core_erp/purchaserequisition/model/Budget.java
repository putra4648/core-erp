package id.putra.core_erp.purchaserequisition.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "budget")
public class Budget extends BaseModel {

    @Id
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department", referencedColumnName = "dept_number")
    private Department department;

    @Column(name = "fy")
    private Date fiscalYear;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetDetail> details;
}
