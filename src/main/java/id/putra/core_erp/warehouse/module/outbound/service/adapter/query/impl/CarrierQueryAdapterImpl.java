package id.putra.core_erp.warehouse.module.outbound.service.adapter.query.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.dto.CarrierDto;
import id.putra.core_erp.warehouse.module.outbound.model.repository.CarrierRepository;
import id.putra.core_erp.warehouse.module.outbound.service.adapter.query.CarrierQueryAdapter;

public class CarrierQueryAdapterImpl implements CarrierQueryAdapter {
    private final CarrierRepository carrierRepository;

    @Override
    public CarrierDto getById(Long id) {
        return carrierRepository.findById(id).map(CarrierDto::toDto).orElse(null);
    }

    @Override
    public List<CarrierDto> getAll(CarrierDto filter) {
        return carrierRepository.findAll().list().stream().map(CarrierDto::toDto).toList();
    }
}
