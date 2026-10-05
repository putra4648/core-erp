package id.putra.core_erp.modules.procurement.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.approval.model.Approval;
import id.putra.core_erp.modules.supplier.model.Supplier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
public class Quotation extends BaseEntity {

	@Column(name = "due_date")
	private Date dueDate;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "suppliers", columnDefinition = "jsonb")
	private List<Supplier> suppliers;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "details", columnDefinition = "jsonb")
	private List<RequisitionDetail> details;

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
	private List<Approval> temApprovals;

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public List<RequisitionDetail> getDetails() {
		return details;
	}

	public void setDetails(List<RequisitionDetail> details) {
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

	public List<Approval> getTemApprovals() {
		return temApprovals;
	}

	public void setTemApprovals(List<Approval> temApprovalDtos) {
		this.temApprovals = temApprovalDtos;
	}

}
