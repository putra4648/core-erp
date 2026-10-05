package id.putra.core_erp.modules.parameter.service;

import id.putra.core_erp.modules.parameter.model.ParameterLookup;
import id.putra.core_erp.modules.parameter.repository.ParameterLookupRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@Transactional
@ApplicationScoped
public class ParameterLookupService implements BaseService<ParameterLookup> {

	private final ParameterLookupRepository parameterLookupRepository;

	public ParameterLookupService(ParameterLookupRepository parameterLookupRepository) {
		this.parameterLookupRepository = parameterLookupRepository;
	}

	public ParameterLookup getParameterLookupByIdOrKey(Long parameterId, String parameterKey) {
		return parameterLookupRepository.findByIdOrKey(parameterId, parameterKey);
	}

	@Override
	public void save(ParameterLookup parameterLookup) {
		parameterLookupRepository.persist(parameterLookup);
	}

	@Override
	public void update(ParameterLookup parameterLookup) {
		parameterLookupRepository.persist(parameterLookup);
	}

	@Override
	public void delete(Long id) {
		parameterLookupRepository.deleteById(id);
	}

	@Override
	public Iterable<ParameterLookup> findAll(ParameterLookup entity, Page page) {
		if (entity == null || entity.getKey() == null || entity.getKey().isBlank()) {
			return parameterLookupRepository.findAll().page(page).list();
		}

		String pattern = "%" + entity.getKey().toLowerCase() + "%";
		return parameterLookupRepository
				.find("lower(key) like ?1 or lower(value) like ?1 or lower(description) like ?1", pattern).page(page)
				.list();
	}

	@Override
	public ParameterLookup findById(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'findById'");
	}

}
