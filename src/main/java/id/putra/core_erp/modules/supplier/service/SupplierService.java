package id.putra.core_erp.modules.supplier.service;

import java.util.List;

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.modules.supplier.repository.SupplierRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class SupplierService implements BaseService<Supplier> {

	private final SupplierRepository supplierRepository;

	public SupplierService(SupplierRepository supplierRepository) {
		this.supplierRepository = supplierRepository;
	}

	public Supplier getById(Long id) {
		return supplierRepository.find("id", id).firstResult();
	}

	@Override
	public void save(Supplier dto) {
		supplierRepository.persist(dto);
	}

	@Override
	public void update(Supplier dto) {
		supplierRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		supplierRepository.deleteById(id);
	}

	@Override
	public Supplier findById(Long id) {
		return supplierRepository.findById(id);
	}

	@Override
	public List<Supplier> findAll(Supplier entity, Page page) {
		return supplierRepository.findAll().page(page).list();
	}

}
