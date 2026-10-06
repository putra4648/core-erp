package id.putra.core_erp.modules.company.model;

import java.util.Date;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "company")
public class Company extends BaseEntity {

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "telp")
	private String telp;

	@Column(name = "npwp")
	private String npwp;

	@Column(name = "logo_path")
	private String logoPath;

	@Column(nullable = false)
	private String accountBank;

	@Column(name = "end_year")
	private Date financialEndYear;

	@Column(name = "currencies")
	private List<String> curr;

	@Column(nullable = false)
	private Boolean needRegisterApproval;

	@Column(nullable = false)
	private Boolean needAutoCloseOrder;

	public String getName() {
		return name;
	}

	public void setName(String compName) {
		this.name = compName;
	}

	public String getTelp() {
		return telp;
	}

	public void setTelp(String compTelp) {
		this.telp = compTelp;
	}

	public String getNpwp() {
		return npwp;
	}

	public void setNpwp(String compNPWP) {
		this.npwp = compNPWP;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String compLogopath) {
		this.logoPath = compLogopath;
	}

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String compAccbank) {
		this.accountBank = compAccbank;
	}

	public Date getFinancialEndYear() {
		return financialEndYear;
	}

	public void setFinancialEndYear(Date compEndyear) {
		this.financialEndYear = compEndyear;
	}

	public List<String> getCurr() {
		return curr;
	}

	public void setCurr(List<String> compCurrencies) {
		this.curr = compCurrencies;
	}

	public Boolean getNeedRegisterApproval() {
		return needRegisterApproval;
	}

	public void setNeedRegisterApproval(Boolean compNeedRegisterApproval) {
		this.needRegisterApproval = compNeedRegisterApproval;
	}

	public Boolean getNeedAutoCloseOrder() {
		return needAutoCloseOrder;
	}

	public void setNeedAutoCloseOrder(Boolean compNeedAutoCloseOrder) {
		this.needAutoCloseOrder = compNeedAutoCloseOrder;
	}

}
