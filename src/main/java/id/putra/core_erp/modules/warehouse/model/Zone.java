package id.putra.core_erp.modules.warehouse.model;

import java.util.List;
import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Table
@Entity
public class Zone extends BaseEntity {

	@NotBlank(message = "Zone code is required")
	public String code;

	@NotBlank(message = "Zone name is required")
	public String name;

	public String type;

	@NotNull(message = "Warehouse is required")
	@ManyToOne
	@JoinColumn(name = "warehouse_id", referencedColumnName = "id")
	public Warehouse warehouse;

	@OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Rack> racks;

}
