package id.putra.core_erp.warehouse.module.outbound.service.core;

import java.util.List;
import id.putra.core_erp.warehouse.module.outbound.dto.ShipmentDto;

public interface ShippingService {
    ShipmentDto create(ShipmentDto dto);
    ShipmentDto update(ShipmentDto dto);
    Boolean delete(Long id);
    ShipmentDto getById(Long id);
    List<ShipmentDto> getAll(ShipmentDto filter);
}

