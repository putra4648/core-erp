package id.putra.core_erp.modules.supplier.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.enums.Status;
import id.putra.core_erp.enums.TypeEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "suppliers")
public class Supplier extends BaseEntity {

   

    @Column(name = "code")
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeEnum type;

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

    @Column(name = "acc_bank")
    private String accBank;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "supplier_logo_path")
    private String supplierLogoPath;

    @Column(name = "supplier_logo_name")
    private String supplierLogoName;

  
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

   

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
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

    public String getAccBank() {
        return accBank;
    }

    public void setAccBank(String accBank) {
        this.accBank = accBank;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSupplierLogoPath() {
        return supplierLogoPath;
    }

    public void setSupplierLogoPath(String supplierLogoPath) {
        this.supplierLogoPath = supplierLogoPath;
    }

    public String getSupplierLogoName() {
        return supplierLogoName;
    }

    public void setSupplierLogoName(String supplierLogoName) {
        this.supplierLogoName = supplierLogoName;
    }

    
}
