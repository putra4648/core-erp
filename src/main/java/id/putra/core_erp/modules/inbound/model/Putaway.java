package id.putra.core_erp.modules.inbound.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Putaway {
	@Id
	private Long id;
	private Long inboundId;
	private String lotNumber;
	private String outboundId;
	private String pickId;
	private String putaway;
	private String sku;

	public Long getId() {
		return id;
	}

	public Long getInboundId() {
		return inboundId;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public String getOutboundId() {
		return outboundId;
	}

	public String getPickId() {
		return pickId;
	}

	public String getPutaway() {
		return putaway;
	}

	public String getSku() {
		return sku;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setInboundId(Long inboundId) {
		this.inboundId = inboundId;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public void setOutboundId(String outboundId) {
		this.outboundId = outboundId;
	}

	public void setPickId(String pickId) {
		this.pickId = pickId;
	}

	public void setPutaway(String putaway) {
		this.putaway = putaway;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}
