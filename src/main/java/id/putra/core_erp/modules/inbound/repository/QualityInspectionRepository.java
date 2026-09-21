package id.putra.core_erp.modules.inbound.repository;

import id.putra.core_erp.modules.inbound.model.QualityInspection;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QualityInspectionRepository implements PanacheRepository<QualityInspection> {

}
