package id.putra.core_erp.modules.procurement.model;

import java.math.BigInteger;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items_measurement")
public class ItemMeasurement extends BaseEntity {

	@Column(name = "unit")
	private String unit;

	// @Column(name = "qty")
	// private BigInteger qty;

	@Column(name = "amount")
	private BigInteger amount;

	@Column(name = "notes")
	private String notes;

	@OneToOne(mappedBy = "measurement")
	private Item item;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigInteger getAmount() {
		return amount;
	}

	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}
