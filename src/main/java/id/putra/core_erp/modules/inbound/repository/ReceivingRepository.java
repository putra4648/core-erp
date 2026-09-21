package id.putra.core_erp.modules.inbound.repository;

import id.putra.core_erp.modules.inbound.model.Receiving;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReceivingRepository implements PanacheRepository<Receiving> {
}
