package id.putra.core_erp.modules.transaction.repository;

import id.putra.core_erp.modules.transaction.model.Transaction;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransactionRepository implements SoftDeleteRepository<Transaction> {

	@Inject
	TransactionItemRepository transactionItemRepository;

	/** Transaction items are lines of the parent transaction — flag them together. */
	@Override
	public void cascadeToChildren(Transaction transaction) {
		transactionItemRepository.find("transaction.id", transaction.id).list()
				.forEach(transactionItemRepository::softDelete);
	}
}
