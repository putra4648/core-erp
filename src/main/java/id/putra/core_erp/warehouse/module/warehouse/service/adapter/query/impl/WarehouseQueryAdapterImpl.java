package id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.warehouse.dto.WarehouseDto;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.WarehouseRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.WarehouseQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class WarehouseQueryAdapterImpl implements WarehouseQueryAdapter {

    @Inject
    private WarehouseRepository warehouseRepository;

    @Override
    public WarehouseDto getWarehouseById(WarehouseDto dto) {
        var wh = warehouseRepository.findById(dto.getId());
        return null;
    }

}
