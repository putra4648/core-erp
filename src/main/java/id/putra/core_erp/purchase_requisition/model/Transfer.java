package id.putra.core_erp.purchase_requisition.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.purchase_requisition.dto.ApprovalDto;
import id.putra.core_erp.purchase_requisition.dto.SupplierDto;
import id.putra.core_erp.purchase_requisition.dto.TransferDetailDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transfer_request")
public class Transfer extends BaseModel {

    @Id
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "due_date")

    private Date dueDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "suppliers", columnDefinition = "jsonb")
    private List<SupplierDto> suppliers;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "details", columnDefinition = "jsonb")
    private List<TransferDetailDto> details;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private String status;

    @Column(name = "reject_note")
    private String rejectNote;

    @Column(name = "current_approval_stage", nullable = false, columnDefinition = "integer default 0")
    private Integer currentApprovalStage;

    @Column(name = "total_approval_stage")
    private Integer totalApprovalStage;

    @Column(name = "last_approved")
    private String lastApproved;

    @ManyToOne
    @JoinColumn(name = "dept_id", referencedColumnName = "dept_number")
    private Department department;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "temp_approvals", columnDefinition = "jsonb")
    private List<ApprovalDto> temApprovalDtos;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "temp_purchaser_approval", columnDefinition = "jsonb")
    private ApprovalDto tempPurchaserApprovalDto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public List<SupplierDto> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<SupplierDto> suppliers) {
        this.suppliers = suppliers;
    }

    public List<TransferDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<TransferDetailDto> details) {
        this.details = details;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public Integer getCurrentApprovalStage() {
        return currentApprovalStage;
    }

    public void setCurrentApprovalStage(Integer currentApprovalStage) {
        this.currentApprovalStage = currentApprovalStage;
    }

    public Integer getTotalApprovalStage() {
        return totalApprovalStage;
    }

    public void setTotalApprovalStage(Integer totalApprovalStage) {
        this.totalApprovalStage = totalApprovalStage;
    }

    public String getLastApproved() {
        return lastApproved;
    }

    public void setLastApproved(String lastApproved) {
        this.lastApproved = lastApproved;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<ApprovalDto> getTemApprovalDtos() {
        return temApprovalDtos;
    }

    public void setTemApprovalDtos(List<ApprovalDto> temApprovalDtos) {
        this.temApprovalDtos = temApprovalDtos;
    }

    public ApprovalDto getTempPurchaserApprovalDto() {
        return tempPurchaserApprovalDto;
    }

    public void setTempPurchaserApprovalDto(ApprovalDto tempPurchaserApprovalDto) {
        this.tempPurchaserApprovalDto = tempPurchaserApprovalDto;
    }

}
