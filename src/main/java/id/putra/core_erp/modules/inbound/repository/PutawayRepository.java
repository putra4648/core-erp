package id.putra.core_erp.modules.inbound.repository;

import id.putra.core_erp.modules.inbound.model.Putaway;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PutawayRepository implements PanacheRepository<Putaway> {
}