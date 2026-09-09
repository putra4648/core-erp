package id.putra.core_erp.warehouse.module.inbound.model.repository;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Receiving;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReceivingRepository implements PanacheRepository<Receiving> {
}
