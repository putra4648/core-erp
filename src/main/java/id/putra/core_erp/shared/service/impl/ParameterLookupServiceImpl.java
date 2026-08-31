package id.putra.core_erp.shared.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.shared.entity.ParameterLookup;
import id.putra.core_erp.shared.repository.ParameterLookupRepository;
import id.putra.core_erp.shared.service.ParameterLookupService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ParameterLookupServiceImpl implements ParameterLookupService {

    private final ParameterLookupRepository parameterLookupRepository;

    @Override
    public Page<ParameterLookup> getAllParameterLookups(String search, Pageable pageable) {
        return parameterLookupRepository.findAll(search, pageable);
    }

    @Override
    public ParameterLookup getParameterLookupByIdOrKey(Long parameterId, String parameterKey) {
        return parameterLookupRepository.findByIdOrKey(parameterId, parameterKey);
    }

    @Override
    public ParameterLookup createParameterLookup(ParameterLookup parameterLookup) {
        return parameterLookupRepository.save(parameterLookup);
    }

    @Override
    public ParameterLookup updateParameterLookup(ParameterLookup parameterLookup) {
        return parameterLookupRepository.save(parameterLookup);
    }

    @Override
    public void deleteParameterLookup(Long id) {
        parameterLookupRepository.deleteById(id);
    }

    @Override
    public void saveAll(List<ParameterLookup> parameterLookups) throws Exception {
        parameterLookupRepository.saveAll(parameterLookups);
    }

}
