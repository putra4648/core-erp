package id.putra.core_erp.modules.parameter.service;

import java.util.List;

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
		parameterLookupRepository.softDeleteById(id);
	}

	@Override
	public List<ParameterLookup> findAll(ParameterLookup entity, Page page) {
		return parameterLookupRepository.findAll().page(page).list();
	}

	@Override
	public ParameterLookup findById(Long id) {
		return parameterLookupRepository.findById(id);
	}

	@Override
	public long count() {
		return parameterLookupRepository.count();
	}
}
