package id.putra.core_erp.modules.outbound.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class PickingTask extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "sales_order_line_id")
	public SalesOrderLine salesOrderLine;

	public String status;


}
