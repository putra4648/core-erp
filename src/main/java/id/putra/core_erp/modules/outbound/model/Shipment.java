package id.putra.core_erp.modules.outbound.model;

import java.sql.Date;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Shipment extends BaseEntity {
	public String shipmentNumber;
	public Date shipDate;
	public String status;

	@ManyToOne
	@JoinColumn(name = "packing_id")
	public Packing packing;

	@ManyToOne
	@JoinColumn(name = "carrier_id")
	public Carrier carrier;

	
}
