package id.putra.core_erp.modules.product.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {

	private String sku;

	private String name;

	private String description;

	private Long minStock;

	private Long maxStock;

	private Boolean isActive;

	private Long quantity;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getMinStock() {
		return minStock;
	}

	public void setMinStock(Long minStock) {
		this.minStock = minStock;
	}

	public Long getMaxStock() {
		return maxStock;
	}

	public void setMaxStock(Long maxStock) {
		this.maxStock = maxStock;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

}
