package id.putra.core_erp.warehouse.module.outbound.service.adapter.query;

import java.util.List;
import id.putra.core_erp.warehouse.module.outbound.dto.CarrierDto;

public interface CarrierQueryAdapter {
    CarrierDto getById(Long id);
    List<CarrierDto> getAll(CarrierDto filter);
}

