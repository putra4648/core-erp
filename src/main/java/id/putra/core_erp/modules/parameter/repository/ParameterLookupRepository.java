package id.putra.core_erp.modules.parameter.repository;

import id.putra.core_erp.modules.parameter.model.ParameterLookup;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParameterLookupRepository implements SoftDeleteRepository<ParameterLookup> {
	public ParameterLookup findByIdOrKey(Long id, String key) {
		return find("id = ?1 or key = ?2", id, key).firstResult();
	}
}
