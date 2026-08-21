package id.putra.core_erp.warehouse.module.inventory.dto;

import java.time.ZonedDateTime;

import id.putra.core_erp.warehouse.shared.base.dto.BaseDto;
import id.putra.core_erp.warehouse.shared.base.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class InventoryItemDto extends BaseDto {
    private Long id;
    private String name;
    private Long quantity;
    private ZonedDateTime expiredDate;
    private Long reservedQty;
    private String lotNumber;
    private ProductDto product;
    private String status;
    private String serialNumber;
}

