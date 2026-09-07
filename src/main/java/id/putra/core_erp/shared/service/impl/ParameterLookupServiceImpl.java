package id.putra.core_erp.shared.service.impl;

import java.util.List;

import id.putra.core_erp.shared.entity.ParameterLookup;
import id.putra.core_erp.shared.repository.ParameterLookupRepository;
import id.putra.core_erp.shared.service.ParameterLookupService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Transactional
public class ParameterLookupServiceImpl implements ParameterLookupService {

    @Inject
    private ParameterLookupRepository parameterLookupRepository;

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

}
