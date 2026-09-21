package id.putra.core_erp.modules.transaction.repository;

import id.putra.core_erp.modules.transaction.model.TransactionItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionItemRepository implements PanacheRepository<TransactionItem> {
}
