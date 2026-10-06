package id.putra.core_erp.modules.warehouse.model;

import java.util.Objects;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "warehouse_racks")
@Entity
public class Rack extends BaseEntity {
<<<<<<< HEAD
	private Long cols;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "rack")
	private List<Location> locations;

	private String name;

	private Long rows;
=======

	private String name;

	private Long rows;

	private Long cols;

	private Boolean isActive;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	@ManyToOne
	@JoinColumn(name = "zone_id")
	private Zone zone;

<<<<<<< HEAD
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
=======
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getRows() {
		return rows;
	}

	public void setRows(Long rows) {
		this.rows = rows;
	}

	public Long getCols() {
		return cols;
	}

	public void setCols(Long cols) {
		this.cols = cols;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cols, isActive, name, rows, zone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Rack other = (Rack) obj;
		return Objects.equals(cols, other.cols) && Objects.equals(isActive, other.isActive)
				&& Objects.equals(name, other.name) && Objects.equals(rows, other.rows)
				&& Objects.equals(zone, other.zone);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}
}
