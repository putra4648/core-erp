package id.putra.core_erp.warehouse.module.warehouse.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Warehouse;

public interface WarehouseCommandAdapter {

    void save(List<Warehouse> dtos);

    void update(List<Warehouse> dtos);

    void delete(List<Long> ids);

}
