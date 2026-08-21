package id.putra.core_erp.warehouse.module.outbound.service.adapter.query;

import java.util.List;
import id.putra.core_erp.warehouse.module.outbound.dto.ShipmentDto;

public interface ShipmentQueryAdapter {
    ShipmentDto getById(Long id);
    List<ShipmentDto> getAll(ShipmentDto filter);
}

