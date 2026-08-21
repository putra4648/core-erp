package id.putra.core_erp.warehouse.module.outbound.service.adapter.command;

import java.util.List;
import id.putra.core_erp.warehouse.module.outbound.dto.ShipmentDto;

public interface ShipmentCommandAdapter {
    void add(List<ShipmentDto> dtos);
    void update(List<ShipmentDto> dtos);
    void delete(List<ShipmentDto> dtos);
}

