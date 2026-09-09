package id.putra.core_erp.warehouse.module.warehouse.service.core.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Warehouse;
import id.putra.core_erp.warehouse.module.warehouse.model.repository.WarehouseRepository;
import id.putra.core_erp.warehouse.module.warehouse.service.core.WarehouseCoreService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class WarehouseServiceImpl implements WarehouseCoreService {

    @Inject
    private WarehouseRepository warehouseRepository;

    @Override
    public Warehouse getWarehouse(Warehouse dto) {
        return warehouseRepository.findById(dto.getId());
    }

    @Override
    public void save(List<Warehouse> dtos) {
        warehouseRepository.persist(dtos);
    }

    @Override
    public void update(List<Warehouse> dtos) {
        dtos.forEach(dto -> warehouseRepository.getEntityManager().merge(dto));
    }

    @Override
    public void delete(List<Long> ids) {
        ids.forEach(warehouseRepository::deleteById);
    }

    @Override
    public long countGetWarehouses(Warehouse warehouse, Page page) {
        return warehouseRepository.count("lower(name) like ?1", warehousePattern(warehouse));
    }

    @Override
    public DataProvider<Warehouse, Void> getWarehouses(Warehouse param, Page page) {
        return DataProvider.fromCallbacks(
                query -> warehouseRepository.find("lower(name) like ?1", warehousePattern(param))
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(countGetWarehouses(param, page)));
    }

    private String warehousePattern(Warehouse warehouse) {
        return "%" + (warehouse.getName() == null ? "" : warehouse.getName().trim().toLowerCase()) + "%";
    }

}
