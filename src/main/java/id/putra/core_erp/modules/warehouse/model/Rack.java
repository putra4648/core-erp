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

	private String name;

	private Long rows;

	private Long cols;

	private Boolean isActive;

	@ManyToOne
	@JoinColumn(name = "zone_id")
	private Zone zone;

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
	}
}
