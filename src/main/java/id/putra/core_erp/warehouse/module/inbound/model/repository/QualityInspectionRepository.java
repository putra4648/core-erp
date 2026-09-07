package id.putra.core_erp.warehouse.module.inbound.model.repository;

import id.putra.core_erp.warehouse.module.inbound.model.entity.QualityInspection;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QualityInspectionRepository implements PanacheRepository<QualityInspection> {

}
