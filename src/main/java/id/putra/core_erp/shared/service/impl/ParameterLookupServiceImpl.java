package id.putra.core_erp.shared.service.impl;

import java.util.List;

import id.putra.core_erp.shared.entity.ParameterLookup;
import id.putra.core_erp.shared.repository.ParameterLookupRepository;
import id.putra.core_erp.shared.service.ParameterLookupService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@Transactional
@ApplicationScoped
public class ParameterLookupServiceImpl implements ParameterLookupService {

    private final ParameterLookupRepository parameterLookupRepository;

    public ParameterLookupServiceImpl(ParameterLookupRepository parameterLookupRepository) {
        this.parameterLookupRepository = parameterLookupRepository;
    }

    @Override
    public ParameterLookup getParameterLookupByIdOrKey(Long parameterId, String parameterKey) {
        return parameterLookupRepository.findByIdOrKey(parameterId, parameterKey);
    }

    @Override
    public void createParameterLookup(ParameterLookup parameterLookup) {
        parameterLookupRepository.persist(parameterLookup);
    }

    @Override
    public void updateParameterLookup(ParameterLookup parameterLookup) {
        parameterLookupRepository.persist(parameterLookup);
    }

    @Override
    public void deleteParameterLookup(Long id) {
        parameterLookupRepository.deleteById(id);
    }

    @Override
    public void persist(List<ParameterLookup> parameterLookups) throws Exception {
        parameterLookupRepository.persist(parameterLookups);
    }

    @Override
    public List<ParameterLookup> getAllParameterLookups(String search, Page page) {
        return parameterLookupRepository.find("search = ?1", search).page(page).list();
    }

}
