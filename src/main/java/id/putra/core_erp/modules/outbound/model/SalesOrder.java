package id.putra.core_erp.modules.outbound.model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.department.model.Department;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table
public class SalesOrder extends BaseEntity {

	@NotBlank(message = "Sales order number is required")
	public String soNumber;
	public LocalDateTime orderDate;
	public String status;

	@OneToMany(mappedBy = "salesOrder")
	public List<SalesOrderLine> salesOrderLines;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	public Department customer;

}
