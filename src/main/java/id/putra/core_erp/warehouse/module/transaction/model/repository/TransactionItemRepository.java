package id.putra.core_erp.warehouse.module.transaction.model.repository;

import id.putra.core_erp.warehouse.module.transaction.model.entity.TransactionItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionItemRepository implements PanacheRepository<TransactionItem> {
}
