package id.putra.core_erp.modules.product.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {

	@NotBlank(message = "SKU is required")
	@Column(name = "sku", unique = true, nullable = false)
	public String sku;

	@NotBlank(message = "Product name is required")
	@Column(name = "name", nullable = false)
	public String name;

	public String description;

	@NotNull(message = "Min stock is required")
	@Min(value = 0, message = "Min stock cannot be negative")
	public Integer minStock;

	@NotNull(message = "Max stock is required")
	@Min(value = 0, message = "Max stock cannot be negative")
	public Integer maxStock;

	@NotNull(message = "Quantity is required")
	public Integer quantity;

	/**
	 * Unit price. {@code columnDefinition} supplies the database default so adding this
	 * column to a populated table succeeds — Hibernate's {@code update} generation emits
	 * {@code add column ... not null} without one, which PostgreSQL rejects on any table
	 * that already has rows. Same reason {@code BaseEntity.deleted} carries a default.
	 */
	@NotNull(message = "Price is required")
	@Column(precision = 19, scale = 4, columnDefinition = "numeric(19,4) default 0")
	public BigDecimal price;

}
