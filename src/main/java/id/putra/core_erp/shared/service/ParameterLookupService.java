package id.putra.core_erp.shared.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import id.putra.core_erp.shared.entity.ParameterLookup;

public interface ParameterLookupService {

    public Page<ParameterLookup> getAllParameterLookups(String search, Pageable pageable);

    public ParameterLookup getParameterLookupByIdOrKey(Long id, String key);

    public ParameterLookup createParameterLookup(ParameterLookup parameterLookup);

    public ParameterLookup updateParameterLookup(ParameterLookup parameterLookup);

    public void deleteParameterLookup(Long id);

    public void saveAll(List<ParameterLookup> parameterLookups) throws Exception;

}
