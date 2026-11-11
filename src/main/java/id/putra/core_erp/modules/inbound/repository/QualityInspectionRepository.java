package id.putra.core_erp.modules.inbound.repository;

import id.putra.core_erp.modules.inbound.model.QualityInspection;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QualityInspectionRepository implements SoftDeleteRepository<QualityInspection> {

}
