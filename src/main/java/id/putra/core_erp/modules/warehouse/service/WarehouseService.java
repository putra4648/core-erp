package id.putra.core_erp.modules.warehouse.service;

import id.putra.core_erp.service.BaseService;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.warehouse.model.Warehouse;
import id.putra.core_erp.modules.warehouse.repository.WarehouseRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class WarehouseService implements BaseService<Warehouse> {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse getWarehouse(Warehouse dto) {
        return warehouseRepository.findById(dto.getId());
    }

    public Warehouse save(Warehouse entity) {
        warehouseRepository.persist(entity);
        return entity;
    }

    public Warehouse findById(Long id) {
        return warehouseRepository.findById(id);
    }

    public Iterable<Warehouse> findAll() {
        return warehouseRepository.findAll().list();
    }

    public void save(List<Warehouse> dtos) {
        warehouseRepository.persist(dtos);
    }

    public void update(List<Warehouse> dtos) {
        dtos.forEach(dto -> warehouseRepository.getEntityManager().merge(dto));
    }

    public void update(Warehouse entity) {
        warehouseRepository.getEntityManager().merge(entity);
    }

    public void delete(List<Long> ids) {
        ids.forEach(warehouseRepository::deleteById);
    }

    public void delete(Long id) {
        warehouseRepository.deleteById(id);
    }

    public long countGetWarehouses(Warehouse warehouse, Page page) {
        return warehouseRepository.count("lower(name) like ?1", warehousePattern(warehouse));
    }

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
