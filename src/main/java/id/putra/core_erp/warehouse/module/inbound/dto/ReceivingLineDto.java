package id.putra.core_erp.warehouse.module.inbound.dto;

import java.math.BigDecimal;
import java.sql.Date;

import id.putra.core_erp.warehouse.module.warehouse.dto.LocationDto;
import id.putra.core_erp.warehouse.shared.base.dto.ProductDto;
import id.putra.core_erp.warehouse.shared.enums.ReceivingStatus;
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
public class ReceivingLineDto {
    private Long id;
    private Date receivedDate;
    private ReceivingStatus status;
    private String lotBatch;
    private Date expiryDate;
    private BigDecimal qty;
    private BigDecimal qtyReceived;
    private ProductDto product;
    private LocationDto location;
}

