package id.putra.core_erp.warehouse.module.supplier.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSupplierIdDto {
    private Long productId;
    private String supplierId;
}

