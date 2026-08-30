package id.putra.core_erp.purchase_requisition.model;

import java.util.List;

import id.putra.core_erp.purchase_requisition.enums.StatusEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "mst_depts")
public class Department extends BaseModel {

    @Id
    @Column(name = "dept_number", nullable = false)
    private String deptNumber;

    @Column(name = "dept_name", nullable = false)
    private String deptName;

    @Column(name = "status")
    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "dept_manager_nrp", referencedColumnName = "nrp")
    private Users deptManager;

    // @Column(name = "other_managers")
    // private List<String> otherManager;

    @ManyToOne()
    @JoinColumn(name = "dept_purchaser_nrp", referencedColumnName = "nrp")
    private Users deptPurchaser;

    @ManyToOne
    @JoinColumn(name = "approval_president_nrp", referencedColumnName = "nrp")
    private Users approvalPresident;

    @OneToMany(mappedBy = "department", orphanRemoval = true)
    private List<Users> users;

    @OneToMany(mappedBy = "department")
    private List<Budget> budgets;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Approval> approvals;

    @OneToMany(mappedBy = "department")
    private List<Requisition> requisitions;
}
