package id.putra.core_erp.modules.outbound.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Shipment {
	@ManyToOne
	@JoinColumn(name = "carrier_id")
	private Carrier carrier;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "packing_id")
	private Packing packing;
	private Date shipDate;

	private String shipmentNumber;

	private String status;

	public Carrier getCarrier() {
		return carrier;
	}

	public Long getId() {
		return id;
	}

	public Packing getPacking() {
		return packing;
	}

	public Date getShipDate() {
		return shipDate;
	}

	public String getShipmentNumber() {
		return shipmentNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPacking(Packing packing) {
		this.packing = packing;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public void setShipmentNumber(String shipmentNumber) {
		this.shipmentNumber = shipmentNumber;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
