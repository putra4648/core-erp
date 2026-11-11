package id.putra.core_erp.modules.procurement.repository;

import java.util.List;

import id.putra.core_erp.modules.procurement.model.RequisitionItem;
import id.putra.core_erp.repository.SoftDeleteRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RequisitionItemRepo implements SoftDeleteRepository<RequisitionItem> {
    public List<RequisitionItem> findByRequisitionId(Long id, Page p) {
        return find("requisition.id = ?1", id).page(p).list();
    }
}
