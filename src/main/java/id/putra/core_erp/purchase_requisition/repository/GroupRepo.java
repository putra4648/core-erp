package id.putra.core_erp.purchase_requisition.repository;

import jakarta.enterprise.context.ApplicationScoped;
import id.putra.core_erp.purchase_requisition.model.Group;
import java.util.Optional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class GroupRepo implements PanacheRepository<Group> {

    public Optional<Group> findByNameContainingIgnoreCase(String name) {
        return find("name ilike ?1", name).firstResultOptional();
    }

}
