package id.putra.core_erp.modules.procurement.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.supplier.model.Supplier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Transfer extends BaseEntity {
	@Column(name = "title")
	public String title;

	@Column(name = "due_date")

	public LocalDate dueDate;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "suppliers", columnDefinition = "jsonb")
	public List<Supplier> suppliers;

	@Column(name = "note")
	public String note;

	@Column(name = "status")
	public String status;

	@ManyToOne
	@JoinColumn(name = "dept_id", referencedColumnName = "dept_number")
	public Department department;

}
