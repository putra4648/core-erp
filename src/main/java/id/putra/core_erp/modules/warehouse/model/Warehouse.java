package id.putra.core_erp.modules.warehouse.model;

import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "warehouses")
@Entity

public class Warehouse extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double area;

	private String location;

	private String name;

	private Integer total;

	@OneToMany(mappedBy = "warehouse", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
	private List<Zone> zones;

	public Double getArea() {
		return area;
	}

	public Long getId() {
		return id;
	}

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public Integer getTotal() {
		return total;
	}

	public List<Zone> getZones() {
		return zones;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}

}
