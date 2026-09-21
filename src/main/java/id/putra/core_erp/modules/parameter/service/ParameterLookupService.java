package id.putra.core_erp.modules.parameter.service;

import java.util.List;

import id.putra.core_erp.modules.parameter.model.ParameterLookup;
import io.quarkus.panache.common.Page;

public interface ParameterLookupService {

    public ParameterLookup getParameterLookupByIdOrKey(Long id, String key);

    public void createParameterLookup(ParameterLookup parameterLookup);

    public void updateParameterLookup(ParameterLookup parameterLookup);

    public void deleteParameterLookup(Long id);

    public void persist(List<ParameterLookup> parameterLookups) throws Exception;

    public List<ParameterLookup> getAllParameterLookups(String search, Page page);

}
