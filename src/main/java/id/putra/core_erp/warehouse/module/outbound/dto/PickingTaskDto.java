package id.putra.core_erp.warehouse.module.outbound.dto;

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
public class PickingTaskDto {
    private Long id;
    private Long salesOrderLineId;
    private String status;
}

