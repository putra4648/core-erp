package id.putra.core_erp.warehouse.module.inventory.service.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.inventory.model.entity.InventoryItem;
import id.putra.core_erp.warehouse.module.inventory.model.repository.InventoryItemRepository;
import id.putra.core_erp.warehouse.module.inventory.service.InventoryItemCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class InventoryItemServiceImpl implements InventoryItemCoreService {
    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemServiceImpl(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Override
    public InventoryItem getInventoryItem(InventoryItem item) {
        return inventoryItemRepository.findById(item.getId());
    }

    @Override
    public void save(List<InventoryItem> items) {
        inventoryItemRepository.persist(items);
    }

    @Override
    public void update(List<InventoryItem> dtos) {
        dtos.forEach(item -> inventoryItemRepository.getEntityManager().merge(item));
    }

    @Override
    public void delete(List<InventoryItem> dtos) {
        dtos.forEach(item -> inventoryItemRepository.deleteById(item.getId()));
    }

    @Override
    public DataProvider<InventoryItem, Void> getInventoryItems(String value, Page page) {
        String pattern = "%" + (value == null ? "" : value.trim().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> inventoryItemRepository.find("lower(name) like ?1", pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(inventoryItemRepository.count("lower(name) like ?1", pattern)));
    }

}
