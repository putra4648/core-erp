package id.putra.core_erp.modules.procurement.repository;

import java.util.List;

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SupplierRepo implements SoftDeleteRepository<Supplier> {

	public List<Supplier> findAllByStatus(String status) {
		return list("status", status);
	}

	public int countByNameContainingIgnoreCase(String name) {
		return (int) count("name ilike ?1", "%" + name + "%");
	}

}
