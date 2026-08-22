package id.putra.core_erp.warehouse.module.supplier.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ProductSupplierId {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "supplier_id")
    private String supplierId;
}
