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
	public String unit;

	// @Column(name = "qty")
	// public BigInteger qty;

	@Column(name = "amount")
	public BigInteger amount;

	@Column(name = "notes")
	public String notes;

	@OneToOne(mappedBy = "measurement")
	public Item item;

}
