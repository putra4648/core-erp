package id.putra.core_erp.modules.outbound.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
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
	}

	public void setShipments(List<Shipment> shipments) {
		this.shipments = shipments;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
}
