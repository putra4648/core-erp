package id.putra.core_erp.modules.supplier.model;

import java.time.Instant;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.TypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "suppliers")
public class Supplier extends BaseEntity {
	@Id
	private Long id;

	@Column(name = "code")
	private String code;

	private String filename;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "phone")
	private String phone;

	private String email;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private TypeEnum type;

	public String getCode() {
		return code;
	}

	public Instant getCreatedDate() {
		return null;
	}

	public String getFilename() {
		return filename;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public TypeEnum getType() {
		return type;
	}

	public Instant getUpdatedDate() {
		return null;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
