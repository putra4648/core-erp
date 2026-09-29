package id.putra.core_erp.modules.approval.model;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.procurement.model.Requisition;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "approval")
public class Approval extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "approval_dept_id", referencedColumnName = "dept_number", insertable = false, updatable = false)
	private Department department;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Boolean isApprovalPermanent;

	private Boolean isBypassPermanent;

	@ManyToOne
	@JoinColumn(name = "requisition_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Requisition requisition;
	private String users;

	public Department getDepartment() {
		return department;
	}

	public Integer getId() {
		return id;
	}

	public Boolean getIsApprovalPermanent() {
		return isApprovalPermanent;
	}

	public Boolean getIsBypassPermanent() {
		return isBypassPermanent;
	}

	public Requisition getRequisition() {
		return requisition;
	}

	public String getUsers() {
		return users;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIsApprovalPermanent(Boolean isApprovalPermanent) {
		this.isApprovalPermanent = isApprovalPermanent;
	}

	public void setIsBypassPermanent(Boolean isBypassPermanent) {
		this.isBypassPermanent = isBypassPermanent;
	}

	public void setRequisition(Requisition requisition) {
		this.requisition = requisition;
	}

	public void setUsers(String users) {
		this.users = users;
	}

}
