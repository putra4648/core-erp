package id.putra.core_erp.modules.approval.model;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.procurement.model.Requisition;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "approval")
public class Approval extends BaseEntity {

	private String users;

	@ManyToOne
	@JoinColumn(name = "approval_dept_id", referencedColumnName = "dept_number", insertable = false, updatable = false)
	private Department department;

	@ManyToOne
	@JoinColumn(name = "requisition_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Requisition requisition;

	public Requisition getRequisition() {
		return requisition;
	}

	public String getUsers() {
		return users;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setRequisition(Requisition requisition) {
		this.requisition = requisition;
	}

	public void setUsers(String users) {
		this.users = users;
	}

}
