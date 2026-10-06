package id.putra.core_erp.modules.outbound.model;

import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
<<<<<<< HEAD
public class Carrier {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String serviceType;

	@OneToMany(mappedBy = "carrier")
	private List<Shipment> shipments;

	public Long getId() {
		return id;
	}
=======
public class Carrier extends BaseEntity {

	private String name;
	private String serviceType;

	@OneToMany(mappedBy = "carrier")
	private List<Shipment> shipments;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	public String getName() {
		return name;
	}

<<<<<<< HEAD
	public String getServiceType() {
		return serviceType;
	}

	public List<Shipment> getShipments() {
		return shipments;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

=======
	public void setName(String name) {
		this.name = name;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public List<Shipment> getShipments() {
		return shipments;
	}

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	public void setShipments(List<Shipment> shipments) {
		this.shipments = shipments;
	}
}
