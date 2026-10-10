package id.putra.core_erp.modules.approval.repository;

import id.putra.core_erp.modules.approval.model.ApprovalRequest;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApprovalRequestRepo implements PanacheRepository<ApprovalRequest> {
}
