package id.putra.core_erp.purchase_requisition.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.purchase_requisition.dto.ApprovalDto;
import id.putra.core_erp.purchase_requisition.dto.RequisitionDetailDto;
import id.putra.core_erp.purchase_requisition.dto.SupplierDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rfq")
@Getter
@Setter
@NoArgsConstructor
public class RFQ extends BaseModel {

    @Id
    private String id;

    @Column(name = "due_date")
    private Date dueDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "suppliers", columnDefinition = "jsonb")
    private List<SupplierDto> suppliers;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "details", columnDefinition = "jsonb")
    private List<RequisitionDetailDto> details;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private String status;

    @Column(name = "reject_note")
    private String rejectNote;

    @Column(name = "current_approval_stage", columnDefinition = "integer default 0")
    private Integer currentApprovalStage;

    @Column(name = "total_approval_stage")
    private Integer totalApprovalStage;

    @Column(name = "last_approved")
    private String lastApproved;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "temp_approvals", columnDefinition = "jsonb")
    private List<ApprovalDto> temApprovalDtos;

}
