package id.putra.core_erp.modules.procurement.repository;

import java.util.List;

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SupplierRepo implements SoftDeleteRepository<Supplier> {

	/**
	 * Suppliers whose code matches exactly.
	 *
	 * <p>Queries {@code code}: {@link Supplier} has no {@code status} field, so the previous
	 * {@code list("status", ...)} failed at parse time with <em>"could not resolve
	 * property"</em>. Code is the caller-facing identifier, so it is the useful filter.
	 */
	public List<Supplier> findAllByCode(String code) {
		return list("code", code);
	}

	public int countByNameContainingIgnoreCase(String name) {
		return (int) count("name ilike ?1", "%" + name + "%");
	}

}
