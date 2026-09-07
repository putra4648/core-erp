package id.putra.core_erp.warehouse.module.warehouse.model.repository;

import id.putra.core_erp.warehouse.module.warehouse.model.entity.Zone;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ZoneRepository implements PanacheRepository<Zone> {
}
