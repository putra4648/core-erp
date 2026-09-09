package id.putra.core_erp.warehouse.module.inbound.model.repository;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Labelling;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LabellingRepository implements PanacheRepository<Labelling> {
}