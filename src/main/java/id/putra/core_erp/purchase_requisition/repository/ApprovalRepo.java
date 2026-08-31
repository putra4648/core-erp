package id.putra.core_erp.purchase_requisition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.putra.core_erp.purchase_requisition.model.Approval;

public interface ApprovalRepo extends JpaRepository<Approval, Integer> {
}
