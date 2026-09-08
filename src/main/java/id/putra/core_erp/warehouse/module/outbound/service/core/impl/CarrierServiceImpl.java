package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.dto.CarrierDto;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.command.CarrierCommandAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.CarrierQueryAdapter;
import id.putra.core_erp.warehouse.module.outbound.service.core.CarrierService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CarrierServiceImpl implements CarrierService {
    @Inject
    private CarrierCommandAdapter carrierCommandAdapter;
    @Inject
    private CarrierQueryAdapter carrierQueryAdapter;

    @Override
    public CarrierDto create(CarrierDto dto) {
        carrierCommandAdapter.add(List.of(dto));
        return dto;
    }

    @Override
    public CarrierDto update(CarrierDto dto) {
        carrierCommandAdapter.update(List.of(dto));
        return dto;
    }

    @Override
    public Boolean delete(Long id) {
        CarrierDto dto = new CarrierDto();
        dto.setId(id);
        carrierCommandAdapter.delete(List.of(dto));
        return true;
    }

    @Override
    public CarrierDto getById(Long id) {
        return carrierQueryAdapter.getById(id);
    }

    @Override
    public List<CarrierDto> getAll(CarrierDto filter) {
        return carrierQueryAdapter.getAll(filter);
    }
}
