package id.putra.core_erp.modules.warehouse.model;

import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "warehouse_racks")
@Entity
public class Rack extends BaseEntity {
	private Long cols;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "rack")
	private List<Location> locations;

	private String name;

	private Long rows;

	@ManyToOne
	@JoinColumn(name = "zone_id")
	private Zone zone;

	public Long getCols() {
		return cols;
	}

	public Long getId() {
		return id;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public String getName() {
		return name;
	}

	public Long getRows() {
		return rows;
	}

	public Zone getZone() {
		return zone;
	}

	public void setCols(Long cols) {
		this.cols = cols;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRows(Long rows) {
		this.rows = rows;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}
}
