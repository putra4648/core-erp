package id.putra.core_erp.warehouse.module.inventory.service.core;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.warehouse.module.inventory.dto.InventoryItemDto;

public interface InventoryItemCoreService {

    Page<InventoryItemDto> getInventoryItems(InventoryItemDto dto, Pageable pageable);

    InventoryItemDto getInventoryItem(InventoryItemDto dto);

    void save(List<InventoryItemDto> dtos);

    void update(List<InventoryItemDto> dtos);

    void delete(List<InventoryItemDto> dtos);

}

