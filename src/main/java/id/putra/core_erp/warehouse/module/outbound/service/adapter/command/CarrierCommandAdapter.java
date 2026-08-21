package id.putra.core_erp.warehouse.module.outbound.service.adapter.command;

import java.util.List;
import id.putra.core_erp.warehouse.module.outbound.dto.CarrierDto;

public interface CarrierCommandAdapter {
    void add(List<CarrierDto> dtos);
    void update(List<CarrierDto> dtos);
    void delete(List<CarrierDto> dtos);
}

