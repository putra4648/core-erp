package id.putra.core_erp.modules.warehouse.model;

import java.util.List;
import java.util.Objects;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table
@Entity
public class Warehouse extends BaseEntity {

	private String name;

	private Boolean isActive;

	private String location;

	private Double area;

	private Integer total;

	@OneToMany(mappedBy = "warehouse", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
	private List<Zone> zones;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<Zone> getZones() {
		return zones;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}

	@Override
	public int hashCode() {
		return Objects.hash(area, isActive, location, name, total, zones);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Warehouse other = (Warehouse) obj;
		return Objects.equals(area, other.area) && Objects.equals(isActive, other.isActive)
				&& Objects.equals(location, other.location) && Objects.equals(name, other.name)
				&& Objects.equals(total, other.total) && Objects.equals(zones, other.zones);
	}

}
