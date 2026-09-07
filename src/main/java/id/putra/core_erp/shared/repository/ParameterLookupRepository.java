package id.putra.core_erp.shared.repository;

import id.putra.core_erp.shared.entity.ParameterLookup;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParameterLookupRepository implements PanacheRepository<ParameterLookup> {
    public ParameterLookup findByIdOrKey(Long id, String key) {
        return find("id = ?1 or key = ?2", id, key).firstResult();
    }
}
