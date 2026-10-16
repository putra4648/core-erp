package id.putra.core_erp.modules.customer.model;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table
public class Customer extends BaseEntity {

	@NotBlank(message = "Customer name is required")
	public String name;

	@Email(message = "Invalid email format")
	public String email;

	public String phone;

}
