package id.putra.core_erp.modules.outbound.model;

import java.math.BigDecimal;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
<<<<<<< HEAD
public class Packing {
	private String dimension;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String packageNumber;
	@OneToMany(mappedBy = "packing")
	private List<Shipment> shipments;

	@Column(precision = 12, scale = 2)
	private BigDecimal weight;

	public String getDimension() {
		return dimension;
	}

	public Long getId() {
		return id;
	}

	public String getPackageNumber() {
		return packageNumber;
	}

	public List<Shipment> getShipments() {
		return shipments;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
=======
public class Packing extends BaseEntity {

	private String packageNumber;
	@Column(precision = 12, scale = 2)
	private BigDecimal weight;
	private String dimension;

	@OneToMany(mappedBy = "packing")
	private List<Shipment> shipments;

	public String getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public List<Shipment> getShipments() {
		return shipments;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public void setShipments(List<Shipment> shipments) {
		this.shipments = shipments;
	}
<<<<<<< HEAD

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
