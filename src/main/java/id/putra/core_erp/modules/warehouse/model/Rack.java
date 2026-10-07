package id.putra.core_erp.modules.warehouse.model;

import java.util.Objects;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Table(name = "warehouse_racks")
@Entity
public class Rack extends BaseEntity {

	@NotBlank(message = "Rack name is required")
	public String name;

	@NotNull(message = "Rows count is required")
	@Min(value = 1, message = "Rows must be at least 1")
	public Long rows;

	@NotNull(message = "Columns count is required")
	@Min(value = 1, message = "Columns must be at least 1")
	public Long cols;

	@NotNull(message = "Zone is required")
	@ManyToOne
	@JoinColumn(name = "zone_id")
	public Zone zone;

}
