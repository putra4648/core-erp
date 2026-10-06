package id.putra.core_erp.modules.outbound.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
<<<<<<< HEAD
public class PickingTask {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
=======
public class PickingTask extends BaseEntity {
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	@ManyToOne
	@JoinColumn(name = "sales_order_line_id")
	private SalesOrderLine salesOrderLine;

	private String status;

<<<<<<< HEAD
	public Long getId() {
		return id;
	}

	public SalesOrderLine getSalesOrderLine() {
		return salesOrderLine;
	}

	public String getStatus() {
		return status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSalesOrderLine(SalesOrderLine salesOrderLine) {
		this.salesOrderLine = salesOrderLine;
	}

	public void setStatus(String status) {
		this.status = status;
	}
=======
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public SalesOrderLine getSalesOrderLine() {
		return salesOrderLine;
	}

	public void setSalesOrderLine(SalesOrderLine salesOrderLine) {
		this.salesOrderLine = salesOrderLine;
	}
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

}
