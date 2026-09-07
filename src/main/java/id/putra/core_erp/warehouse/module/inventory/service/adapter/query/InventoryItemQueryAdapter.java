package id.putra.core_erp.warehouse.module.inventory.service.adapter.query;

import id.putra.core_erp.warehouse.module.inventory.dto.InventoryItemDto;

public interface InventoryItemQueryAdapter {

    InventoryItemDto getInventoryItemById(InventoryItemDto dto);

}
