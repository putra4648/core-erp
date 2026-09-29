package id.putra.core_erp.modules.company.model;

import java.util.Date;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "company")
public class Company extends BaseEntity {
	@Column(nullable = false)
	private String accountBank;

	@Column(name = "currencies")
	private List<String> curr;

	@Column(name = "end_year")
	private Date financialEndYear;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "logo_path")
	private String logoPath;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private Boolean needAutoCloseOrder;

	@Column(nullable = false)
	private Boolean needRegisterApproval;

	@Column(name = "npwp")
	private String npwp;

	@Column(name = "telp")
	private String telp;

	public String getAccountBank() {
		return accountBank;
	}

	public List<String> getCurr() {
		return curr;
	}

	public Date getFinancialEndYear() {
		return financialEndYear;
	}

	public Long getId() {
		return id;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public String getName() {
		return name;
	}

	public Boolean getNeedAutoCloseOrder() {
		return needAutoCloseOrder;
	}

	public Boolean getNeedRegisterApproval() {
		return needRegisterApproval;
	}

	public String getNpwp() {
		return npwp;
	}

	public String getTelp() {
		return telp;
	}

	public void setAccountBank(String compAccbank) {
		this.accountBank = compAccbank;
	}

	public void setCurr(List<String> compCurrencies) {
		this.curr = compCurrencies;
	}

	public void setFinancialEndYear(Date compEndyear) {
		this.financialEndYear = compEndyear;
	}

	public void setId(Long compId) {
		this.id = compId;
	}

	public void setLogoPath(String compLogopath) {
		this.logoPath = compLogopath;
	}

	public void setName(String compName) {
		this.name = compName;
	}

	public void setNeedAutoCloseOrder(Boolean compNeedAutoCloseOrder) {
		this.needAutoCloseOrder = compNeedAutoCloseOrder;
	}

	public void setNeedRegisterApproval(Boolean compNeedRegisterApproval) {
		this.needRegisterApproval = compNeedRegisterApproval;
	}

	public void setNpwp(String compNPWP) {
		this.npwp = compNPWP;
	}

	public void setTelp(String compTelp) {
		this.telp = compTelp;
	}

}
