package id.putra.core_erp.modules.inventory.service;

import id.putra.core_erp.shared.service.BaseService;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.inventory.model.InventoryItem;
import id.putra.core_erp.modules.inventory.repository.InventoryItemRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class InventoryItemCoreService implements BaseService<InventoryItem> {
    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemCoreService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }
    public InventoryItem getInventoryItem(InventoryItem item) {
        return inventoryItemRepository.findById(item.getId());
    }
    public InventoryItem findById(Long id) {
        return inventoryItemRepository.findById(id);
    }
    public InventoryItem save(InventoryItem entity) {
        inventoryItemRepository.persist(entity);
        return entity;
    }
    public Iterable<InventoryItem> findAll() {
        return inventoryItemRepository.findAll().list();
    }
    public void save(List<InventoryItem> items) {
        inventoryItemRepository.persist(items);
    }
    public void update(List<InventoryItem> dtos) {
        dtos.forEach(item -> inventoryItemRepository.getEntityManager().merge(item));
    }
    public void update(InventoryItem entity) {
        inventoryItemRepository.getEntityManager().merge(entity);
    }
    public void delete(List<InventoryItem> dtos) {
        dtos.forEach(item -> inventoryItemRepository.deleteById(item.getId()));
    }
        public void delete(Long id) {
            inventoryItemRepository.deleteById(id);
        }
    public DataProvider<InventoryItem, Void> getInventoryItems(String value, Page page) {
        String pattern = "%" + (value == null ? "" : value.trim().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> inventoryItemRepository.find("lower(name) like ?1", pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(inventoryItemRepository.count("lower(name) like ?1", pattern)));
    }

}
