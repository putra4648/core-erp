package id.putra.core_erp.modules.supplier.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
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

    @Id
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    // @Column(name = "status")
    // @Enumerated(EnumType.STRING)
    // private Status status;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeEnum type;

    @Column(name = "phone")
    private String phone;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_po_address_id", referencedColumnName = "id")
    private SupplierPOAddress supplierPOAddress;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_address_id", referencedColumnName = "id")
    private SupplierAddress supplierAddress;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SupplierContact> contacts = new ArrayList<>();

    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMainContact() {
        return (contacts != null && !contacts.isEmpty()) ? contacts.get(0).getContactName() : "";
    }

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
