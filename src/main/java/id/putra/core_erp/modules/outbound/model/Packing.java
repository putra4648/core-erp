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
public class Packing extends BaseEntity {

	public String packageNumber;
	@Column(precision = 12, scale = 2)
	public BigDecimal weight;
	public String dimension;

	@OneToMany(mappedBy = "packing")
	public List<Shipment> shipments;

	
}
