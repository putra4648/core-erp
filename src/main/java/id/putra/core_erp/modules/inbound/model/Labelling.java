package id.putra.core_erp.modules.inbound.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
<<<<<<< HEAD
public class Labelling {
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
=======
public class Labelling extends BaseEntity {

	private String sku;
	private String lotNumber;
	private Long inboundId;
	private String outboundId;
	private String pickId;
	private String putaway;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public Long getInboundId() {
		return inboundId;
	}

	public void setInboundId(Long inboundId) {
		this.inboundId = inboundId;
	}

	public String getOutboundId() {
		return outboundId;
	}

	public void setOutboundId(String outboundId) {
		this.outboundId = outboundId;
	}

	public String getPickId() {
		return pickId;
	}

	public void setPickId(String pickId) {
		this.pickId = pickId;
	}

	public String getPutaway() {
		return putaway;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public void setPutaway(String putaway) {
		this.putaway = putaway;
	}
<<<<<<< HEAD

	public void setSku(String sku) {
		this.sku = sku;
	}
=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

}
