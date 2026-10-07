package id.putra.core_erp.modules.inventory.model;

import java.time.ZonedDateTime;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.product.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "inventory_item")
@Entity
public class InventoryItem extends BaseEntity {
	public String name;
	public Long reservedQty;
	public String lotNumber;
	public ZonedDateTime expiredDate;
	public String status;
	public String serialNumber;
	public Long quantity;

	@ManyToOne
	@JoinColumn(name = "product_sku")
	public Product product;

}
