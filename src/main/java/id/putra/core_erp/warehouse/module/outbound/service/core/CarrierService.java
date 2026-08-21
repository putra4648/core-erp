package id.putra.core_erp.warehouse.module.outbound.service.core;

import java.util.List;
import id.putra.core_erp.warehouse.module.outbound.dto.CarrierDto;

public interface CarrierService {
    CarrierDto create(CarrierDto dto);
    CarrierDto update(CarrierDto dto);
    Boolean delete(Long id);
    CarrierDto getById(Long id);
    List<CarrierDto> getAll(CarrierDto filter);
}

