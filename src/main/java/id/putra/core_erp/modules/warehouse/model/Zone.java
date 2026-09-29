package id.putra.core_erp.modules.warehouse.model;

import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "warehouse_zones")
@Entity

public class Zone extends BaseEntity {
	private String code;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Rack> racks;

	private String type;

	@ManyToOne
	@JoinColumn(name = "warehouse_id", referencedColumnName = "id")
	private Warehouse warehouse;

	public String getCode() {
		return code;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Rack> getRacks() {
		return racks;
	}

	public String getType() {
		return type;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRacks(List<Rack> racks) {
		this.racks = racks;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

}
