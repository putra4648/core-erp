package id.putra.core_erp.modules.customer.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
<<<<<<< HEAD
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
=======
import jakarta.persistence.Table;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

@Entity
@Table
public class Customer extends BaseEntity {
<<<<<<< HEAD
	@NotBlank
	private String code;

	private String email;

	private String phone;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;

	public String getCode() {
		return code;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

=======

	private String name;

	private Boolean isActive;

	private String email;

	private String phone;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
