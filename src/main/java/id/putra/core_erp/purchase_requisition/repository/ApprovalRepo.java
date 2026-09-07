package id.putra.core_erp.purchase_requisition.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import id.putra.core_erp.purchase_requisition.model.Approval;

@ApplicationScoped
public class ApprovalRepo implements PanacheRepository<Approval> {
    
}
