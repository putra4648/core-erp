package id.putra.core_erp.modules.product.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {

	@NotBlank(message = "SKU is required")
	public String sku;

	@NotBlank(message = "Product name is required")
	public String name;

	public String description;

	@NotNull(message = "Min stock is required")
	@Min(value = 0, message = "Min stock cannot be negative")
	public Long minStock;

	@NotNull(message = "Max stock is required")
	@Min(value = 0, message = "Max stock cannot be negative")
	public Long maxStock;

	@NotNull(message = "Quantity is required")
	@Min(value = 0, message = "Quantity cannot be negative")
	public Long quantity;

}
