package id.putra.core_erp.modules.inventory.repository;

import id.putra.core_erp.modules.inventory.model.InventoryItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryItemRepository implements PanacheRepository<InventoryItem> {
}
