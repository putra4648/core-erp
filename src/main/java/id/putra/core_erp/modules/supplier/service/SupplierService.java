package id.putra.core_erp.modules.supplier.service;

import java.util.List;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.modules.supplier.repository.SupplierRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }
    public Supplier getById(Long id) {
        return supplierRepository.find("id", id).firstResult();
    }
    public List<Supplier> create(List<Supplier> dto) {
        supplierRepository.persist(dto);
        return dto;
    }
    public void update(Long id, Supplier dto) {
        supplierRepository.getEntityManager().merge(dto);
    }
    public void delete(Long id) {
        Supplier supplier = getById(id);
        if (supplier != null) {
            supplierRepository.delete(supplier);
        }
    }
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
