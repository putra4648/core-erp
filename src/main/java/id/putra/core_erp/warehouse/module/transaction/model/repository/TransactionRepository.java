package id.putra.core_erp.warehouse.module.transaction.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.putra.core_erp.warehouse.module.transaction.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

