package id.putra.core_erp.modules.outbound.model;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class SalesOrderLine extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "product_id")
	public Product product;

	@ManyToOne
	@JoinColumn(name = "sales_order_id")
	public SalesOrder salesOrder;


}
