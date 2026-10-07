package id.putra.core_erp.modules.procurement.model;

import java.math.BigInteger;
import java.util.Date;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory extends BaseEntity {

	@Column(name = "po_number")
	public String poNumber;

	@Column(name = "part_name")
	public String partName;

	@Column(name = "description")
	public String description;

	@Column(name = "area")
	public String area;

	@Column(name = "shelf")
	public String shelf;

	@Column(name = "supplier")
	public String supplier;

	@Column(name = "unit")
	public String unit;

	@Column(name = "qty")
	public BigInteger qty;

	@Column(name = "item_area")
	public String itemArea;

	@Column(name = "date")
	public Date date;

	@Column(name = "location")
	public String location;

	@Column(name = "uom")
	public String uom;

	

}
