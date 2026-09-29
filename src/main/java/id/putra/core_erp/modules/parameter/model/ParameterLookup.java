package id.putra.core_erp.modules.parameter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "parameter_lookup")

public class ParameterLookup {

	@NotBlank
	private String description;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String key;

	private String module;

	@NotBlank
	private String value;

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public String getModule() {
		return module;
	}

	public String getValue() {
		return value;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
