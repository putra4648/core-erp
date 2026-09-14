package id.putra.core_erp.warehouse.module.transaction.model.repository;

import id.putra.core_erp.warehouse.module.transaction.model.entity.Transaction;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionRepository implements PanacheRepository<Transaction> {
}
