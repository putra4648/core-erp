package id.putra.core_erp.purchaserequisition.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

