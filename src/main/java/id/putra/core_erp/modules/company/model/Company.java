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
<<<<<<< HEAD
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
=======

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "telp")
	private String telp;

	@Column(name = "npwp")
	private String npwp;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	@Column(name = "logo_path")
	private String logoPath;

<<<<<<< HEAD
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
=======
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
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

}
