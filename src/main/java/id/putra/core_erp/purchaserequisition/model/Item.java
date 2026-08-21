package id.putra.core_erp.purchaserequisition.model;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

import id.putra.core_erp.shared.entity.Category;
import id.putra.core_erp.shared.entity.Supplier;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item extends BaseModel {
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

    @Column(name = "updated_date")
    private Instant updatedDate;

    @Override
    public Instant getUpdatedDate() {
        return this.updatedDate;
    }

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

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    public String getImagePath() {
        return this.itemImagePath;
    }

    public List<String> getDocsPath() {
        return this.docsPath;
    }
}
