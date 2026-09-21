package id.putra.core_erp.modules.procurement.model;

import java.math.BigInteger;
import java.util.List;

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.shared.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items")
public class Item extends BaseEntity {
    @Id
    @Column(name = "item_number", nullable = false)
    private String itemNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private BigInteger price;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    private String unit;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "acc_no")
    private Supplier supplier;

    @Column(name = "other_supplier")
    private String otherSupplier;

    @Column(name = "gl_acc")
    private String glAcc;

    @Column(name = "item_image_path")
    private String itemImagePath;

    @Column(name = "docs_path")
    private List<String> docsPath;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "measurement_id", referencedColumnName = "id")
    private ItemMeasurement measurement;

    // @ManyToOne
    // @JoinColumn(name = "category_id", referencedColumnName = "id")
    // private Category category;

    public String getImagePath() {
        return this.itemImagePath;
    }

    public List<String> getDocsPath() {
        return this.docsPath;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getOtherSupplier() {
        return otherSupplier;
    }

    public void setOtherSupplier(String otherSupplier) {
        this.otherSupplier = otherSupplier;
    }

    public String getGlAcc() {
        return glAcc;
    }

    public void setGlAcc(String glAcc) {
        this.glAcc = glAcc;
    }

    public String getItemImagePath() {
        return itemImagePath;
    }

    public void setItemImagePath(String itemImagePath) {
        this.itemImagePath = itemImagePath;
    }

    public void setDocsPath(List<String> docsPath) {
        this.docsPath = docsPath;
    }

    public ItemMeasurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(ItemMeasurement measurement) {
        this.measurement = measurement;
    }

    // public Category getCategory() {
    //     return category;
    // }

    // public void setCategory(Category category) {
    //     this.category = category;
    // }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
