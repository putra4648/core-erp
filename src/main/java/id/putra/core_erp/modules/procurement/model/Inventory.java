package id.putra.core_erp.modules.procurement.model;

import java.math.BigInteger;
import java.util.Date;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory extends BaseEntity {

	@Column(name = "area")
	private String area;

	@Column(name = "date")
	private Date date;

	@Column(name = "description")
	private String description;

	@Id
	private Long id;

	@Column(name = "item_area")
	private String itemArea;

	@Column(name = "location")
	private String location;

	@Column(name = "part_name")
	private String partName;

	@Column(name = "part_number")
	private String partNumber;

	@Column(name = "po_number")
	private String poNumber;

	@Column(name = "qty")
	private BigInteger qty;

	@Column(name = "shelf")
	private String shelf;

	@Column(name = "supplier")
	private String supplier;

	@Column(name = "unit")
	private String unit;

	@Column(name = "uom")
	private String uom;

	public String getArea() {
		return area;
	}

	public Date getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}

	public String getItemArea() {
		return itemArea;
	}

	public String getLocation() {
		return location;
	}

	public String getPartName() {
		return partName;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public BigInteger getQty() {
		return qty;
	}

	public String getShelf() {
		return shelf;
	}

	public String getSupplier() {
		return supplier;
	}

	public String getUnit() {
		return unit;
	}

	public String getUom() {
		return uom;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setItemArea(String itemArea) {
		this.itemArea = itemArea;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public void setQty(BigInteger qty) {
		this.qty = qty;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

}
