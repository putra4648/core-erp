package id.putra.core_erp.warehouse.module.transaction.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import id.putra.core_erp.warehouse.module.inventory.model.entity.InventoryItem;
import id.putra.core_erp.shared.entity.BaseEntity;
import id.putra.core_erp.shared.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "transaction_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class TransactionItem extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "source_inv_item_id")
    private InventoryItem sourceInventoryItem;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "dest_inv_item_id")
    private InventoryItem destInventoryItem;

    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String lotNumber;
    private String serialNumber;
    private String notes;
}


