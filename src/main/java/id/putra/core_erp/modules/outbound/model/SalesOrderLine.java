package id.putra.core_erp.modules.outbound.model;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
<<<<<<< HEAD
public class SalesOrderLine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
=======
public class SalesOrderLine extends BaseEntity {
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "sales_order_id")
	private SalesOrder salesOrder;

<<<<<<< HEAD
	public Long getId() {
		return id;
	}

	public Product getProduct() {
		return product;
=======
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public SalesOrder getSalesOrder() {
		return salesOrder;
	}

<<<<<<< HEAD
	public void setId(Long id) {
		this.id = id;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	public void setSalesOrder(SalesOrder salesOrder) {
		this.salesOrder = salesOrder;
	}
}
