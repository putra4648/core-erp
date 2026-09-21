package id.putra.core_erp.modules.inbound.repository;

import id.putra.core_erp.modules.inbound.model.Labelling;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LabellingRepository implements PanacheRepository<Labelling> {
}