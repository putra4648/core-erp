package id.putra.core_erp.modules.procurement.repository;

import java.util.Optional;

import id.putra.core_erp.modules.procurement.model.Item;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItemRepo implements SoftDeleteRepository<Item> {

	/**
	 * Item owns its measurement (one-to-one, {@code CascadeType.ALL} on the entity), so
	 * the measurement row must be flagged too — otherwise an orphaned measurement stays
	 * queryable while its item is hidden.
	 */
	@Override
	public void cascadeToChildren(Item item) {
		if (item.measurement != null) {
			item.measurement.markDeleted(currentActor());
		}
	}

	public long countByNameContainingIgnoreCase(String name) {
		return count("name ilike ?1", name);
	}

	public Optional<Item> findByNameEqualsIgnoreCase(String name) {
		return find("name ilike ?1", name).firstResultOptional();
	}

	public Optional<Item> findByItemNumber(String name) {
		return find("itemNumber ilike ?1", name).firstResultOptional();
	}

}
