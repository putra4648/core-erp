package id.putra.core_erp.modules.company.model;

import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "company")
public class Company extends BaseEntity {

	@NotBlank(message = "Telephone is required")
	@Column(name = "telp", nullable = false)
	public String telp;

	@NotBlank(message = "NPWP is required")
	@Column(name = "npwp", nullable = false)
	public String npwp;

	@Column(name = "logo_path")
	public String logoPath;

	@NotBlank(message = "Bank account is required")
	@Column(name = "account_bank", nullable = false)
	public String accountBank;

	@Column(name = "currencies")
	public List<String> curr;

}
