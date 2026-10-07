package id.putra.core_erp.modules.warehouse.model;

import java.util.List;
import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Table
@Entity
public class Warehouse extends BaseEntity {

	@NotBlank(message = "Warehouse name is required")
	public String name;

	public String location;

	@PositiveOrZero(message = "Area cannot be negative")
	public Double area;

	public Integer total;

	@OneToMany(mappedBy = "warehouse", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
	public List<Zone> zones;

}
