package id.putra.core_erp.modules.warehouse.model;

import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.inbound.model.ReceivingLine;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "warehouse_locations")
@Entity
public class Location extends BaseEntity {
	private String binNumber;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rack_id")
	private Rack rack;

	@OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
	private List<ReceivingLine> receivingLines;

	private String type;

	public String getBinNumber() {
		return binNumber;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Rack getRack() {
		return rack;
	}

	public List<ReceivingLine> getReceivingLines() {
		return receivingLines;
	}

	public String getType() {
		return type;
	}

	public void setBinNumber(String binNumber) {
		this.binNumber = binNumber;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRack(Rack rack) {
		this.rack = rack;
	}

	public void setReceivingLines(List<ReceivingLine> receivingLines) {
		this.receivingLines = receivingLines;
	}

	public void setType(String type) {
		this.type = type;
	}

}
