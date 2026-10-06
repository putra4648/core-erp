package id.putra.core_erp.modules.procurement.repository;

import java.util.List;

import id.putra.core_erp.enums.Status;
import id.putra.core_erp.modules.supplier.model.Supplier;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SupplierRepo implements PanacheRepository<Supplier> {

<<<<<<< HEAD
	public int countByNameContainingIgnoreCase(String name) {
		return (int) count("name ilike ?1", "%" + name + "%");
	}

	public List<Supplier> findAllByStatus(String status) {
		return list("status", status);
	}

	public Optional<Supplier> findByAccNo(String accNo) {
		return find("accNo = ?1", accNo).singleResultOptional();
	}

	public List<Supplier> findByStatus(Status status) {
		return list("status", status);
	}

	//
	// findAllByNameContainingIgnoreCaseAndStatusStartingWithIgnoreCase(String name,
	// String status,
	// Pageable pageable);
=======
	public List<Supplier> findAllByStatus(String status) {
		return list("status", status);
	}

	public int countByNameContainingIgnoreCase(String name) {
		return (int) count("name ilike ?1", "%" + name + "%");
	}

	public List<Supplier> findByStatus(Status status) {
		return list("status", status);
	}

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
