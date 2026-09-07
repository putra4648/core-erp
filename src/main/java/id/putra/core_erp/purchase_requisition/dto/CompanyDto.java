package id.putra.core_erp.purchase_requisition.dto;

public class CompanyDto {
    private Long id;
    private String name;
    private String telp;
    private String npwp;
    private String logo_name;
    private String logo_image;
    private String accountBank;
    private String financialEndYear;
    private Boolean isRegisterApproval;
    private Boolean isAutoCloseOrder;
    private String curr1;
    private String curr2;
    private String curr3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }

    public String getLogo_name() {
        return logo_name;
    }

    public void setLogo_name(String logo_name) {
        this.logo_name = logo_name;
    }

    public String getLogo_image() {
        return logo_image;
    }

    public void setLogo_image(String logo_image) {
        this.logo_image = logo_image;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getFinancialEndYear() {
        return financialEndYear;
    }

    public void setFinancialEndYear(String financialEndYear) {
        this.financialEndYear = financialEndYear;
    }

    public Boolean getIsRegisterApproval() {
        return isRegisterApproval;
    }

    public void setIsRegisterApproval(Boolean isRegisterApproval) {
        this.isRegisterApproval = isRegisterApproval;
    }

    public Boolean getIsAutoCloseOrder() {
        return isAutoCloseOrder;
    }

    public void setIsAutoCloseOrder(Boolean isAutoCloseOrder) {
        this.isAutoCloseOrder = isAutoCloseOrder;
    }

    public String getCurr1() {
        return curr1;
    }

    public void setCurr1(String curr1) {
        this.curr1 = curr1;
    }

    public String getCurr2() {
        return curr2;
    }

    public void setCurr2(String curr2) {
        this.curr2 = curr2;
    }

    public String getCurr3() {
        return curr3;
    }

    public void setCurr3(String curr3) {
        this.curr3 = curr3;
    }
}
