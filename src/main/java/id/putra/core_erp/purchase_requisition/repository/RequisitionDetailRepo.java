package id.putra.core_erp.purchase_requisition.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import id.putra.core_erp.purchase_requisition.model.RequisitionDetail;
import id.putra.core_erp.shared.entity.Supplier;

public interface RequisitionDetailRepo extends JpaRepository<RequisitionDetail, String> {
    List<RequisitionDetail> findBySupplier(Supplier supplier);
}
