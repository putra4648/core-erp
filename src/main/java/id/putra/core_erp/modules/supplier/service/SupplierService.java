package id.putra.core_erp.modules.supplier.service;

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
<<<<<<< HEAD

	public SupplierService(SupplierRepository supplierRepository) {
		this.supplierRepository = supplierRepository;
	}

	public List<Supplier> create(List<Supplier> dto) {
		supplierRepository.persist(dto);
		return dto;
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
						.page(Page.of(query.getOffset() / query.getLimit(), query.getLimit())).list().stream(),
				query -> Math
						.toIntExact(supplierRepository.count("lower(name) like ?1 or lower(code) like ?1", pattern)));
	}

	public Supplier getById(Long id) {
		return supplierRepository.find("id", id).firstResult();
	}

	public void update(Long id, Supplier dto) {
		supplierRepository.getEntityManager().merge(dto);
	}
=======

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
	public Iterable<Supplier> findAll(Supplier entity, Page page) {
		return supplierRepository.findAll().page(page).list();
	}

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
