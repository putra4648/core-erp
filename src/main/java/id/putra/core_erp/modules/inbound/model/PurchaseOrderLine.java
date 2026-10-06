package id.putra.core_erp.modules.inbound.model;

import java.math.BigDecimal;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class PurchaseOrderLine extends BaseEntity {

<<<<<<< HEAD
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private BigDecimal price;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
=======
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	private Integer quantity;
	private BigDecimal price;

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	@ManyToOne
	@JoinColumn(name = "purchase_order_id")
	private PurchaseOrder purchaseOrder;

<<<<<<< HEAD
	private Integer quantity;

	public Long getId() {
		return id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Product getProduct() {
		return product;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
=======
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
