package id.putra.core_erp.purchase_requisition.model;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.purchase_requisition.dto.ApprovalDto;
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
@Table(name = "requisition")
public class Requisition extends BaseModel {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "note")
    private String note;

    @Column(name = "reject_note")
    private String rejectNote;

    @Column(name = "currency")
    private BigInteger currency;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "investment_id")
    private String investmentId;

    @Column(name = "unit_price")
    private BigInteger unitPrice;

    @Column(name = "status")
    private String status;

    @Column(name = "budget_detail_id")
    private String budgetDetailId;

    @Column(name = "budget_date")
    private Date budgetDate;

    @Column(name = "current_approval_stage", columnDefinition = "integer default 0")
    private Integer currentApprovalStage;

    @Column(name = "total_approval_stage")
    private Integer totalApprovalStage;

    @Column(name = "last_approved")
    private String lastApproved;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "temp_approvals", columnDefinition = "jsonb")
    private List<ApprovalDto> temApprovalDtos;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "temp_purchaser_approval", columnDefinition = "jsonb")
    private ApprovalDto tempPurchaserApprovalDto;

    @ManyToOne
    @JoinColumn(name = "budget_id", referencedColumnName = "id")
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "dept_id", referencedColumnName = "dept_number")
    private Department department;

    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequisitionDetail> details;
}
