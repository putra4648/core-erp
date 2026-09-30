package id.putra.core_erp.modules.product.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {
	@Id
	private Long id;

	private String description;

	private Long maxStock;

	@Min(value = 1)
	private Long minStock;

	@NotBlank
	private String name;

	@Min(value = 1)
	private Long quantity;

	@NotBlank
	private String sku;

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}

	public Long getMaxStock() {
		return maxStock;
	}

	public Long getMinStock() {
		return minStock;
	}

	public String getName() {
		return name;
	}

	public Long getQuantity() {
		return quantity;
	}

	public String getSku() {
		return sku;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMaxStock(Long maxStock) {
		this.maxStock = maxStock;
	}

	public void setMinStock(Long minStock) {
		this.minStock = minStock;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	// public Category getCategory() {
	// return category;
	// }

	// public void setCategory(Category category) {
	// this.category = category;
	// }
}
