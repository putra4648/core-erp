package id.putra.core_erp.purchase_requisition.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "company")
public class Company extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comp_id", nullable = false)
    private Long compId;

    @Column(name = "comp_name", nullable = false, unique = true)
    private String compName;

    @Column(name = "comp_telp")
    private String compTelp;

    @Column(name = "comp_npwp")
    private String compNPWP;

    @Column(name = "comp_logoname")
    private String compLogopath;

    // @Lob
    // @Column(name = "comp_logoblob")
    // private byte[] compLogoblob;

    @Column(name = "comp_accbank")
    private String compAccbank;

    @Column(name = "comp_endyear")
    private Date compEndyear;

    @Column(name = "comp_currencies")
    private List<String> compCurrencies;

    @Column(name = "comp_needRegisterApproval")
    private Boolean compNeedRegisterApproval;

    @Column(name = "comp_needAutoCloseOrder")
    private Boolean compNeedAutoCloseOrder;

    public Long getId() {
        return compId;
    }

    public void setId(Long compId) {
        this.compId = compId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompTelp() {
        return compTelp;
    }

    public void setCompTelp(String compTelp) {
        this.compTelp = compTelp;
    }

    public String getCompNPWP() {
        return compNPWP;
    }

    public void setCompNPWP(String compNPWP) {
        this.compNPWP = compNPWP;
    }

    public String getCompLogopath() {
        return compLogopath;
    }

    public void setCompLogopath(String compLogopath) {
        this.compLogopath = compLogopath;
    }

    public String getCompAccbank() {
        return compAccbank;
    }

    public void setCompAccbank(String compAccbank) {
        this.compAccbank = compAccbank;
    }

    public Date getCompEndyear() {
        return compEndyear;
    }

    public void setCompEndyear(Date compEndyear) {
        this.compEndyear = compEndyear;
    }

    public List<String> getCompCurrencies() {
        return compCurrencies;
    }

    public void setCompCurrencies(List<String> compCurrencies) {
        this.compCurrencies = compCurrencies;
    }

    public Boolean getCompNeedRegisterApproval() {
        return compNeedRegisterApproval;
    }

    public void setCompNeedRegisterApproval(Boolean compNeedRegisterApproval) {
        this.compNeedRegisterApproval = compNeedRegisterApproval;
    }

    public Boolean getCompNeedAutoCloseOrder() {
        return compNeedAutoCloseOrder;
    }

    public void setCompNeedAutoCloseOrder(Boolean compNeedAutoCloseOrder) {
        this.compNeedAutoCloseOrder = compNeedAutoCloseOrder;
    }

}
