package id.putra.core_erp.purchaserequisition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.putra.core_erp.purchaserequisition.model.Approval;

public interface ApprovalRepo extends JpaRepository<Approval, Integer> {
}

