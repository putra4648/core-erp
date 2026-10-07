package id.putra.core_erp.modules.parameter.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "parameter_lookup")

public class ParameterLookup extends BaseEntity {

	public String module;

	@NotBlank(message = "Key is required")
	public String key;

	@NotBlank(message = "Value is required")
	public String value;

	public String description;

}
