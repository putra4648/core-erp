package id.putra.core_erp.modules.warehouse.model;

import java.util.List;
import java.util.Objects;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table
@Entity
public class Zone extends BaseEntity {

	private String code;

	private String name;

	private Boolean isActive;

	private String type;

	@ManyToOne
	@JoinColumn(name = "warehouse_id", referencedColumnName = "id")
	private Warehouse warehouse;

	@OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Rack> racks;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public List<Rack> getRacks() {
		return racks;
	}

	public void setRacks(List<Rack> racks) {
		this.racks = racks;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, isActive, name, racks, type, warehouse);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Zone other = (Zone) obj;
		return Objects.equals(code, other.code) && Objects.equals(isActive, other.isActive)
				&& Objects.equals(name, other.name) && Objects.equals(racks, other.racks)
				&& Objects.equals(type, other.type) && Objects.equals(warehouse, other.warehouse);
	}

}
