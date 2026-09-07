package id.putra.core_erp.shared.service;

import java.util.List;

import id.putra.core_erp.shared.entity.ParameterLookup;

public interface ParameterLookupService {

    public ParameterLookup getParameterLookupByIdOrKey(Long id, String key);

    public void createParameterLookup(ParameterLookup parameterLookup);

    public void updateParameterLookup(ParameterLookup parameterLookup);

    public void deleteParameterLookup(Long id);

    public void persist(List<ParameterLookup> parameterLookups) throws Exception;

}
