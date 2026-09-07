package id.putra.core_erp.shared.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import id.putra.core_erp.shared.enums.TypeEnum;
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

    @Id
    @Column(name = "acc_no", nullable = false)
    private String accNo;

    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "website")
    private String website;

    @Column(name = "status")
    private String status;

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

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_po_address_id", referencedColumnName = "id")
    private SupplierPOAddress supplierPOAddress;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_address_id", referencedColumnName = "id")
    private SupplierAddress supplierAddress;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SupplierContact> contacts = new ArrayList<>();

    public String getMainContact() {
        return (contacts != null && !contacts.isEmpty()) ? contacts.get(0).getContactName() : "";
    }

    public Instant getCreatedDate() {
        return null;
    }

    public Instant getUpdatedDate() {
        return null;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public SupplierPOAddress getSupplierPOAddress() {
        return supplierPOAddress;
    }

    public void setSupplierPOAddress(SupplierPOAddress supplierPOAddress) {
        this.supplierPOAddress = supplierPOAddress;
    }

    public SupplierAddress getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(SupplierAddress supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public List<SupplierContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<SupplierContact> contacts) {
        this.contacts = contacts;
    }
}
