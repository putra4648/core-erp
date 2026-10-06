package id.putra.core_erp.modules.supplier.model;

import java.time.Instant;

import id.putra.core_erp.entity.BaseEntity;
<<<<<<< HEAD
import id.putra.core_erp.enums.TypeEnum;
import jakarta.persistence.CascadeType;
=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "suppliers")
public class Supplier extends BaseEntity {

<<<<<<< HEAD
	@Id
	private Long id;

	@Column(name = "code")
	private String code;

	@Column(name = "name", nullable = false)
	private String name;

	private String email;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private TypeEnum type;

	@Column(name = "phone")
	private String phone;

	public Instant getCreatedDate() {
		return null;
	}

	public Instant getUpdatedDate() {
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

=======
	@Column(name = "code")
	private String code;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "phone")
	private String phone;

	@Column(name = "fax")
	private String fax;

	@Column(name = "currency")
	private String currency;

	@Column(name = "npwp")
	private String npwp;

	@Column(name = "bank_account")
	private String bankAccount;

	@Column(name = "notes", columnDefinition = "TEXT")
	private String notes;

	public Instant getCreatedDate() {
		return null;
	}

	public Instant getUpdatedDate() {
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getNpwp() {
		return npwp;
	}

	public void setNpwp(String npwp) {
		this.npwp = npwp;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String accBank) {
		this.bankAccount = accBank;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
