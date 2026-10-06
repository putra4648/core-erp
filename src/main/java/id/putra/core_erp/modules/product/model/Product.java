package id.putra.core_erp.modules.product.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {

<<<<<<< HEAD
	private String description;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long maxStock;

	@Min(value = 1)
	private Long minStock;

	@NotBlank
	private String name;

	@Min(value = 1)
	private Long quantity;

	@NotBlank
	private String sku;

	// @ManyToOne
	// @JoinColumn(name = "category_id")
	// private Category category;

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
=======
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
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

<<<<<<< HEAD
	public void setSku(String sku) {
		this.sku = sku;
	}

	// public Category getCategory() {
	// return category;
	// }

	// public void setCategory(Category category) {
	// this.category = category;
	// }
=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
