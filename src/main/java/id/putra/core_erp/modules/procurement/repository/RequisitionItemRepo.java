package id.putra.core_erp.modules.procurement.repository;

import java.util.List;

import id.putra.core_erp.modules.procurement.model.RequisitionItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RequisitionItemRepo implements PanacheRepository<RequisitionItem> {
    public List<RequisitionItem> findByRequisitionId(Long id, Page p) {
        return find("requisition.id = ?1", id).page(p).list();
    }
}
