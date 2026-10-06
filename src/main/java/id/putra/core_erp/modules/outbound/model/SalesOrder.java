package id.putra.core_erp.modules.outbound.model;

import java.sql.Date;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.department.model.Department;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
<<<<<<< HEAD
public class SalesOrder {
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Department customer;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date orderDate;
	@OneToMany(mappedBy = "salesOrder")
	private List<SalesOrderLine> salesOrderLines;

	private String soNumber;

	private String status;

	public Department getCustomer() {
		return customer;
	}

	public Long getId() {
		return id;
=======
public class SalesOrder extends BaseEntity {
	private String soNumber;
	private Date orderDate;
	private String status;

	@OneToMany(mappedBy = "salesOrder")
	private List<SalesOrderLine> salesOrderLines;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Department customer;

	public String getSoNumber() {
		return soNumber;
	}

	public void setSoNumber(String soNumber) {
		this.soNumber = soNumber;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public Date getOrderDate() {
		return orderDate;
	}

<<<<<<< HEAD
	public List<SalesOrderLine> getSalesOrderLines() {
		return salesOrderLines;
	}

	public String getSoNumber() {
		return soNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setCustomer(Department customer) {
		this.customer = customer;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public void setSalesOrderLines(List<SalesOrderLine> salesOrderLines) {
		this.salesOrderLines = salesOrderLines;
	}

	public void setSoNumber(String soNumber) {
		this.soNumber = soNumber;
	}

	public void setStatus(String status) {
		this.status = status;
=======
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<SalesOrderLine> getSalesOrderLines() {
		return salesOrderLines;
	}

	public void setSalesOrderLines(List<SalesOrderLine> salesOrderLines) {
		this.salesOrderLines = salesOrderLines;
	}

	public Department getCustomer() {
		return customer;
	}

	public void setCustomer(Department customer) {
		this.customer = customer;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}
}
