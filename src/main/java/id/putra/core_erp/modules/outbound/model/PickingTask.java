package id.putra.core_erp.modules.outbound.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class PickingTask {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sales_order_line_id")
	private SalesOrderLine salesOrderLine;

	private String status;

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

}
