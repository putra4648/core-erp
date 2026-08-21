package id.putra.core_erp.warehouse.module.inventory.service.adapter.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.inventory.dto.InventoryItemDto;

public interface InventoryItemQueryAdapter {

    Page<InventoryItemDto> getInventoryItems(InventoryItemDto dto, Pageable pageable);

    InventoryItemDto getInventoryItemById(InventoryItemDto dto);

}

