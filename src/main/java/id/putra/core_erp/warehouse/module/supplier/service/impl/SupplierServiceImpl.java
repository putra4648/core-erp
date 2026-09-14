package id.putra.core_erp.warehouse.module.supplier.service.impl;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.shared.entity.Supplier;
import id.putra.core_erp.warehouse.module.supplier.model.repository.SupplierRepository;
import id.putra.core_erp.warehouse.module.supplier.service.SupplierService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier getById(Long id) {
        return supplierRepository.find("id", id).firstResult();
    }

    @Override
    public List<Supplier> create(List<Supplier> dto) {
        supplierRepository.persist(dto);
        return dto;
    }

    @Override
    public void update(Long id, Supplier dto) {
        supplierRepository.getEntityManager().merge(dto);
    }

    @Override
    public void delete(Long id) {
        Supplier supplier = getById(id);
        if (supplier != null) {
            supplierRepository.delete(supplier);
        }
    }

    @Override
    public DataProvider<Supplier, Void> getAll(String value, Page page) {
        String pattern = "%" + (value == null ? "" : value.trim().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> supplierRepository.find("lower(name) like ?1 or lower(code) like ?1", pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math
                        .toIntExact(supplierRepository.count("lower(name) like ?1 or lower(code) like ?1", pattern)));
    }
}
