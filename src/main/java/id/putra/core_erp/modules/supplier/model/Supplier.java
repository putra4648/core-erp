package id.putra.core_erp.modules.supplier.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "suppliers")
public class Supplier extends BaseEntity {

	@NotBlank(message = "Supplier code is required")
	@Column(name = "code")
	public String code;

	@NotBlank(message = "Supplier name is required")
	@Column(name = "name", nullable = false)
	public String name;

	@Email(message = "Invalid email format")
	@Column(name = "email")
	public String email;

	@Column(name = "phone")
	public String phone;

	@Column(name = "fax")
	public String fax;

	@Column(name = "currency")
	public String currency;

	@Column(name = "npwp")
	public String npwp;

	@Column(name = "bank_account")
	public String bankAccount;

	@Column(name = "notes", columnDefinition = "TEXT")
	public String notes;

}
