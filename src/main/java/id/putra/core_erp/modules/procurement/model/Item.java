package id.putra.core_erp.modules.procurement.model;

import java.math.BigInteger;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items")
public class Item extends BaseEntity {
	@Column(name = "description")
	private String description;

	@Column(name = "docs_path")
	private List<String> docsPath;

	@Column(name = "gl_acc")
	private String glAcc;

	@Column(name = "item_image_path")
	private String itemImagePath;

	@Id
	@Column(name = "item_number", nullable = false)
	private String itemNumber;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "measurement_id", referencedColumnName = "id")
	private ItemMeasurement measurement;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "other_supplier")
	private String otherSupplier;

	@Column(name = "price")
	private BigInteger price;

	@Column(name = "status")
	private String status;

	private String unit;

	// @ManyToOne
	// @JoinColumn(name = "category_id", referencedColumnName = "id")
	// private Category category;

	public String getDescription() {
		return description;
	}

	public List<String> getDocsPath() {
		return this.docsPath;
	}

	public String getGlAcc() {
		return glAcc;
	}

	public String getImagePath() {
		return this.itemImagePath;
	}

	public String getItemImagePath() {
		return itemImagePath;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public ItemMeasurement getMeasurement() {
		return measurement;
	}

	public String getName() {
		return name;
	}

	public String getOtherSupplier() {
		return otherSupplier;
	}

	public BigInteger getPrice() {
		return price;
	}

	public String getStatus() {
		return status;
	}

	public String getUnit() {
		return unit;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDocsPath(List<String> docsPath) {
		this.docsPath = docsPath;
	}

	public void setGlAcc(String glAcc) {
		this.glAcc = glAcc;
	}

	public void setItemImagePath(String itemImagePath) {
		this.itemImagePath = itemImagePath;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public void setMeasurement(ItemMeasurement measurement) {
		this.measurement = measurement;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOtherSupplier(String otherSupplier) {
		this.otherSupplier = otherSupplier;
	}

	public void setPrice(BigInteger price) {
		this.price = price;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
