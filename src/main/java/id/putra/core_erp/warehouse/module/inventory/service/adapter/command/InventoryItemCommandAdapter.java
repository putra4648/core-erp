package id.putra.core_erp.warehouse.module.inventory.service.adapter.command;

import java.util.List;

import id.putra.core_erp.warehouse.module.inventory.dto.InventoryItemDto;

public interface InventoryItemCommandAdapter {

    void save(List<InventoryItemDto> dtos);

    void update(List<InventoryItemDto> dtos);

    void delete(List<InventoryItemDto> dtos);

}

