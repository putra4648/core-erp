package id.putra.core_erp.modules.procurement.repository;

import id.putra.core_erp.modules.procurement.model.Approval;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApprovalRepo implements PanacheRepository<Approval> {
    
}
