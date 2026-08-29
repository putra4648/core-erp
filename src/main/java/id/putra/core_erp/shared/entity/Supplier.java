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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
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

    @Builder.Default
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

    @Builder.Default
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SupplierContact> contacts = new ArrayList<>();

    public String getMainContact() {
        return (contacts != null && !contacts.isEmpty()) ? contacts.get(0).getContactName() : "";
    }

    public String getCountry() {
        return (supplierAddress != null) ? supplierAddress.getCountry() : "";
    }

    public Instant getCreatedDate() {
        return null;
    }

    public Instant getUpdatedDate() {
        return null;
    }
}
