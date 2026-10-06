package id.putra.core_erp.modules.parameter.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "parameter_lookup")

public class ParameterLookup extends BaseEntity {

<<<<<<< HEAD
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
=======
	private String module;

	private String key;

	private String value;

	private String description;

	private Boolean isActive;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public String getKey() {
		return key;
	}

<<<<<<< HEAD
	public String getModule() {
		return module;
=======
	public void setKey(String key) {
		this.key = key;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public String getValue() {
		return value;
	}

<<<<<<< HEAD
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
=======
	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

}
