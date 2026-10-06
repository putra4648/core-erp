package id.putra.core_erp.modules.budget.model;

import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.department.model.Department;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "budget")
public class Budget extends BaseEntity {

	@ManyToOne(optional = false)
	@JoinColumn(name = "department", referencedColumnName = "dept_number")
	private Department department;

<<<<<<< HEAD
	@OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BudgetDetail> details;

	@Column(name = "fy")
	private Date fiscalYear;

	@Id
	private String id;
=======
	@Column(name = "fiscal_year")
	private String fiscalYear;

	private Long allocated;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BudgetDetail> details;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	public Department getDepartment() {
		return department;
	}

<<<<<<< HEAD
	public List<BudgetDetail> getDetails() {
		return details;
	}

	public Date getFiscalYear() {
		return fiscalYear;
	}

	public String getId() {
		return id;
	}

	public void setDepartment(Department department) {
		this.department = department;
=======
	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public List<BudgetDetail> getDetails() {
		return details;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public void setDetails(List<BudgetDetail> details) {
		this.details = details;
	}

<<<<<<< HEAD
	public void setFiscalYear(Date fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public void setId(String id) {
		this.id = id;
=======
	public Long getAllocated() {
		return allocated;
	}

	public void setAllocated(Long allocated) {
		this.allocated = allocated;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}
}
