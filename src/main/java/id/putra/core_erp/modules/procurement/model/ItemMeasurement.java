package id.putra.core_erp.modules.procurement.model;

import java.math.BigInteger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items_measurement")
public class ItemMeasurement {
	@Column(name = "amount")
	private BigInteger amount;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	// @Column(name = "qty")
	// private BigInteger qty;

	@OneToOne(mappedBy = "measurement")
	private Item item;

	@Column(name = "notes")
	private String notes;

	@Column(name = "unit")
	private String unit;

	public BigInteger getAmount() {
		return amount;
	}

	public Integer getId() {
		return id;
	}

	public Item getItem() {
		return item;
	}

	public String getNotes() {
		return notes;
	}

	public String getUnit() {
		return unit;
	}

	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
