package id.putra.core_erp.modules.outbound.model;

import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class Carrier extends BaseEntity {

	public String name;
	public String serviceType;

	@OneToMany(mappedBy = "carrier")
	public List<Shipment> shipments;

	
}
