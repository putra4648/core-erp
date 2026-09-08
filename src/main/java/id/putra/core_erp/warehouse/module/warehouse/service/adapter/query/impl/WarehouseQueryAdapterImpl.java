package id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.impl;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Warehouse;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.WarehouseRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.adapter.query.WarehouseQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class WarehouseQueryAdapterImpl implements WarehouseQueryAdapter {

    @Inject
    private WarehouseRepository warehouseRepository;

    @Override
    public Warehouse getWarehouseById(Warehouse dto) {
        return warehouseRepository.findById(dto.getId());
    }

}
