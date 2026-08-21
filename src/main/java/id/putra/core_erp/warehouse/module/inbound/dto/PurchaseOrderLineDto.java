package id.putra.core_erp.warehouse.module.inbound.dto;

import java.math.BigDecimal;

import id.putra.core_erp.warehouse.shared.base.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderLineDto {
    private Long id;
    private ProductDto product;
    private Integer quantity;
    private BigDecimal price;
}

