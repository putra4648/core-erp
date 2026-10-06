package id.putra.core_erp.modules.procurement.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.approval.model.Approval;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.supplier.model.Supplier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Transfer extends BaseEntity {
	@Column(name = "title")
	private String title;

<<<<<<< HEAD
	@Column(name = "current_approval_stage", nullable = false, columnDefinition = "integer default 0")
	private Integer currentApprovalStage;

	@ManyToOne
	@JoinColumn(name = "dept_id", referencedColumnName = "dept_number")
	private Department department;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "details", columnDefinition = "jsonb")
	private List<TransferDetail> details;

	@Column(name = "due_date")

	private Date dueDate;

	private String filename;

	@Id
	private Long id;

	@Column(name = "last_approved")
	private String lastApproved;

	@Column(name = "note")
	private String note;

	@Column(name = "reject_note")
	private String rejectNote;

	@Column(name = "status")
	private String status;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "suppliers", columnDefinition = "jsonb")
	private List<Supplier> suppliers;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "temp_approvals", columnDefinition = "jsonb")
	private List<Approval> temApprovals;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "temp_purchaser_approval", columnDefinition = "jsonb")
	private Approval tempPurchaserApproval;

	@Column(name = "title")
	private String title;

	@Column(name = "total_approval_stage")
	private Integer totalApprovalStage;

	public Integer getCurrentApprovalStage() {
		return currentApprovalStage;
	}

	public Department getDepartment() {
		return department;
	}

	public List<TransferDetail> getDetails() {
		return details;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public String getFilename() {
		return filename;
	}

	public Long getId() {
		return id;
	}

	public String getLastApproved() {
		return lastApproved;
	}

	public String getNote() {
		return note;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public String getStatus() {
		return status;
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public List<Approval> getTemApprovals() {
		return temApprovals;
	}

	public Approval getTempPurchaserApproval() {
		return tempPurchaserApproval;
	}

	public String getTitle() {
		return title;
	}

	public Integer getTotalApprovalStage() {
		return totalApprovalStage;
=======
	@Column(name = "due_date")

	private Date dueDate;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "suppliers", columnDefinition = "jsonb")
	private List<Supplier> suppliers;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "details", columnDefinition = "jsonb")
	private List<TransferDetail> details;

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
	private List<Approval> temApprovals;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "temp_purchaser_approval", columnDefinition = "jsonb")
	private Approval tempPurchaserApproval;

	private String filename;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public List<TransferDetail> getDetails() {
		return details;
	}

	public void setDetails(List<TransferDetail> details) {
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
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public void setCurrentApprovalStage(Integer currentApprovalStage) {
		this.currentApprovalStage = currentApprovalStage;
	}

<<<<<<< HEAD
	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setDetails(List<TransferDetail> details) {
		this.details = details;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLastApproved(String lastApproved) {
		this.lastApproved = lastApproved;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public void setTemApprovals(List<Approval> temApprovalDtos) {
		this.temApprovals = temApprovalDtos;
	}

	public void setTempPurchaserApproval(Approval tempPurchaserApprovalDto) {
		this.tempPurchaserApproval = tempPurchaserApprovalDto;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTotalApprovalStage(Integer totalApprovalStage) {
		this.totalApprovalStage = totalApprovalStage;
	}
=======
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

	public List<Approval> getTemApprovals() {
		return temApprovals;
	}

	public void setTemApprovals(List<Approval> temApprovalDtos) {
		this.temApprovals = temApprovalDtos;
	}

	public Approval getTempPurchaserApproval() {
		return tempPurchaserApproval;
	}

	public void setTempPurchaserApproval(Approval tempPurchaserApprovalDto) {
		this.tempPurchaserApproval = tempPurchaserApprovalDto;
	}
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

}
