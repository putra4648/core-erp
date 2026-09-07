package id.putra.core_erp.purchase_requisition.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "approval")
public class Approval extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "approval_nrp", referencedColumnName = "nrp", insertable = false, updatable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "approval_dept_id", referencedColumnName = "dept_number", insertable = false, updatable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "requisition_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Requisition requisition;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Requisition getRequisition() {
        return requisition;
    }

    public void setRequisition(Requisition requisition) {
        this.requisition = requisition;
    }

    // @Column(name = "is_approved")
    // private Boolean isApproved;

    // @Column(name = "keepPermanentApproved")
    // private Boolean keepPermanent;

    // @Column(name = "keepPermanentBypass")
    // private Boolean keepPermanentBypass;

    // @Column(name = "is_bypass")
    // private Boolean isBypass;

    // @Column(name = "bypass_reason")
    // private String bypassReason;

}
