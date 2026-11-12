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

	/**
	 * Number of items whose name contains {@code name}, case-insensitively.
	 *
	 * <p>The wildcards are added here, so callers pass a bare fragment rather than a
	 * {@code like} pattern. Without them {@code ilike} is an exact match and the method
	 * would contradict its own name — which is exactly the bug this carries the fix for.
	 * {@code SupplierRepo#countByNameContainingIgnoreCase} already wraps its own wildcards;
	 * this now behaves the same way.
	 */
	public long countByNameContainingIgnoreCase(String name) {
		return count("name ilike ?1", "%" + name + "%");
	}

	/**
	 * Case-insensitive exact-name lookup.
	 *
	 * <p>{@code ilike} with no surrounding {@code %} is an exact match, and on PostgreSQL
	 * that is genuinely case-insensitive — unlike {@code like}, which is case-sensitive
	 * on this database.
	 */
	public Optional<Item> findByNameEqualsIgnoreCase(String name) {
		return find("name ilike ?1", name).firstResultOptional();
	}

	/**
	 * Case-insensitive lookup by item name.
	 *
	 * <p>Queries {@code name}: {@link Item} has no {@code itemNumber} field, so a query on
	 * that property fails at parse time with <em>"could not resolve property"</em>. The
	 * method name is historical — the only identifying string on the entity is the name.
	 */
	public Optional<Item> findByItemNumber(String name) {
		return find("name ilike ?1", name).firstResultOptional();
	}

}
