package id.putra.core_erp.modules.parameter.service;

import java.util.List;

import id.putra.core_erp.modules.parameter.model.ParameterLookup;
import id.putra.core_erp.modules.parameter.repository.ParameterLookupRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@Transactional
@ApplicationScoped
public class ParameterLookupService {

    private final ParameterLookupRepository parameterLookupRepository;

    public ParameterLookupService(ParameterLookupRepository parameterLookupRepository) {
        this.parameterLookupRepository = parameterLookupRepository;
    }
    public ParameterLookup getParameterLookupByIdOrKey(Long parameterId, String parameterKey) {
        return parameterLookupRepository.findByIdOrKey(parameterId, parameterKey);
    }
    public void createParameterLookup(ParameterLookup parameterLookup) {
        parameterLookupRepository.persist(parameterLookup);
    }
    public void updateParameterLookup(ParameterLookup parameterLookup) {
        parameterLookupRepository.persist(parameterLookup);
    }
    public void deleteParameterLookup(Long id) {
        parameterLookupRepository.deleteById(id);
    }
    public void persist(List<ParameterLookup> parameterLookups) throws Exception {
        parameterLookupRepository.persist(parameterLookups);
    }
    public List<ParameterLookup> getAllParameterLookups(String search, Page page) {
        if (search == null || search.isBlank()) {
            return parameterLookupRepository.findAll().page(page).list();
        }

        String pattern = "%" + search.toLowerCase() + "%";
        return parameterLookupRepository.find(
                "lower(key) like ?1 or lower(value) like ?1 or lower(description) like ?1",
                pattern).page(page).list();
    }

}
