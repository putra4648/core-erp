package id.putra.core_erp.modules.procurement.model;

import java.math.BigInteger;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items_measurement")
<<<<<<< HEAD
public class ItemMeasurement {
	@Column(name = "amount")
	private BigInteger amount;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
=======
public class ItemMeasurement extends BaseEntity {

	@Column(name = "unit")
	private String unit;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	// @Column(name = "qty")
	// private BigInteger qty;

<<<<<<< HEAD
	@OneToOne(mappedBy = "measurement")
	private Item item;
=======
	@Column(name = "amount")
	private BigInteger amount;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	@Column(name = "notes")
	private String notes;

<<<<<<< HEAD
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
=======
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
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public void setItem(Item item) {
		this.item = item;
	}
<<<<<<< HEAD

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
