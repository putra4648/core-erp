package id.putra.core_erp.modules.procurement.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "items")
public class Item extends BaseEntity {

	@NotBlank(message = "Item name is required")
	@Column(name = "name", nullable = false)
	public String name;

	@NotNull(message = "Price is required")
	@PositiveOrZero(message = "Price cannot be negative")
	@Column(name = "price", nullable = false)
	public BigDecimal price;

	@Column(name = "description")
	public String description;

	@NotBlank(message = "Unit of measure is required")
	public String unit;

	@Column(name = "other_supplier")
	public String otherSupplier;

	@Column(name = "gl_acc")
	public String glAcc;

	@Column(name = "item_image_path")
	public String itemImagePath;

	@Column(name = "docs_path")
	public List<String> docsPath;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "measurement_id", referencedColumnName = "id")
	public ItemMeasurement measurement;

}
