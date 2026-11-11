package id.putra.core_erp.modules.inventory.repository;

import id.putra.core_erp.modules.inventory.model.InventoryItem;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryItemRepository implements SoftDeleteRepository<InventoryItem> {
}
