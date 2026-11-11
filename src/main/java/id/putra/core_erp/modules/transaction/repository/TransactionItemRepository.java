package id.putra.core_erp.modules.transaction.repository;

import id.putra.core_erp.modules.transaction.model.TransactionItem;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionItemRepository implements SoftDeleteRepository<TransactionItem> {
}
