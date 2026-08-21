package id.putra.core_erp.warehouse.module.outbound.dto;

import java.math.BigDecimal;
import java.util.List;

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
public class PackingDto {
    private Long id;
    private String packageNumber;
    private BigDecimal weight;
    private String dimension;
    private List<Long> shipmentIds;
}

