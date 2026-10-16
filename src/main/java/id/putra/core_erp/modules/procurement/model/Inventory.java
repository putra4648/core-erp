package id.putra.core_erp.modules.procurement.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory extends BaseEntity {

	@Column(name = "name")
	public String name;

	@Column(name = "description")
	public String description;

	@Column(name = "unit")
	public String unit;

	@Column(name = "qty")
	public BigInteger qty;

	@Column(name = "date")
	public LocalDateTime date;

}
