package id.putra.core_erp.purchase_requisition.repository;

import jakarta.enterprise.context.ApplicationScoped;
import id.putra.core_erp.purchase_requisition.model.RequisitionDetail;
import id.putra.core_erp.shared.entity.Supplier;
import java.util.List;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class RequisitionDetailRepo implements PanacheRepository<RequisitionDetail> {

    public List<RequisitionDetail> findBySupplier(Supplier supplier) {
        return find("supplier", supplier).list();
    }

}
