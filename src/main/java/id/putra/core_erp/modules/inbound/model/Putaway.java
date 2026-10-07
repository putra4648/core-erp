package id.putra.core_erp.modules.inbound.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
public class Putaway extends BaseEntity {

	public String sku;
	public String lotNumber;
	public Long inboundId;
	public String outboundId;
	public String pickId;
	public String putaway;

	

}
