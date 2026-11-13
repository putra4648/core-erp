package id.putra.core_erp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.modules.procurement.model.Item;
import id.putra.core_erp.modules.procurement.model.ItemMeasurement;
import id.putra.core_erp.modules.procurement.repository.ItemRepo;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers {@link ItemRepo}: the {@code ilike} searches and the one-to-one cascade.
 *
 * <p>The cascade case matters because {@code Item.measurement} is a separate row with its
 * own soft delete flag. If {@code cascadeToChildren} is dropped, the item disappears from
 * every query while its measurement stays live and queryable — an orphan nothing else
 * would catch.
 */
@QuarkusTest
class ItemRepoTest {

	@Inject
	ItemRepo itemRepo;

	@Inject
	EntityManager em;

	private Item newItem(String name, String unit) {
		Item item = new Item();
		item.name = name;
		item.price = new BigDecimal("25.00");
		item.unit = unit;
		item.description = "Test item " + name;

		ItemMeasurement measurement = new ItemMeasurement();
		measurement.unit = unit;
		measurement.notes = "fixture";
		item.measurement = measurement;
		return item;
	}

	@Test
	@TestTransaction
	@DisplayName("findByNameEqualsIgnoreCase matches a different case")
	void findByNameIsCaseInsensitive() {
		Item item = newItem("Widget Alpha", "PCS");
		itemRepo.persist(item);
		em.flush();

		assertTrue(itemRepo.findByNameEqualsIgnoreCase("widget alpha").isPresent(),
				"ilike must match a lowercase query against a mixed-case name");
		assertTrue(itemRepo.findByNameEqualsIgnoreCase("WIDGET ALPHA").isPresent(),
				"ilike must match an uppercase query against a mixed-case name");
		assertTrue(itemRepo.findByNameEqualsIgnoreCase("Widget Alpha").isPresent(),
				"ilike must match the exact name");
	}

	@Test
	@TestTransaction
	@DisplayName("findByNameEqualsIgnoreCase is an exact match, not a contains match")
	void findByNameIsExactMatch() {
		Item item = newItem("Widget Beta", "PCS");
		itemRepo.persist(item);
		em.flush();

		assertTrue(itemRepo.findByNameEqualsIgnoreCase("Widget").isEmpty(),
				"a prefix must not match — ilike here has no surrounding wildcard");
		assertTrue(itemRepo.findByNameEqualsIgnoreCase("Widget Beta Extra").isEmpty(),
				"a longer string must not match");
	}

	@Test
	@TestTransaction
	@DisplayName("countByNameContainingIgnoreCase matches on a substring")
	void countByNameIsContainsMatch() {
		itemRepo.persist(newItem("Contained Widget", "PCS"));
		itemRepo.persist(newItem("Widget Contained", "PCS"));
		itemRepo.persist(newItem("Unrelated Thing", "PCS"));
		em.flush();
		em.clear();

		long count = itemRepo.countByNameContainingIgnoreCase("contained");

		assertEquals(2L, count, "count must include both items whose name contains the term");
	}

	@Test
	@TestTransaction
	@DisplayName("findByItemNumber resolves by name, since no itemNumber field exists")
	void findByItemNumberQueriesName() {
		Item item = newItem("Numbered Item", "PCS");
		itemRepo.persist(item);
		em.flush();

		assertTrue(itemRepo.findByItemNumber("numbered item").isPresent(),
				"the lookup must resolve the item by its name");
		assertTrue(itemRepo.findByItemNumber("no such item").isEmpty());
	}

	@Test
	@TestTransaction
	@DisplayName("soft-deleting an item cascades to its measurement")
	void softDeleteCascadesToMeasurement() {
		Item item = newItem("Cascade Item", "BOX");
		itemRepo.persist(item);
		em.flush();

		Long itemId = item.id;
		Long measurementId = item.measurement.id;
		assertNotNull(measurementId, "fixture must have persisted the measurement row");

		itemRepo.softDeleteById(itemId);
		em.flush();
		em.clear();

		assertNull(itemRepo.findById(itemId), "item must be hidden");

		// The measurement row must still exist, but flagged.
		Object flag = em.createNativeQuery("select deleted from items_measurement where id = ?1")
				.setParameter(1, measurementId).getSingleResult();
		assertEquals(Boolean.TRUE, flag,
				"measurement must be flagged too, otherwise it stays live while its item is hidden");
	}

	@Test
	@TestTransaction
	@DisplayName("a soft-deleted item is invisible to every search")
	void softDeletedItemIsHiddenFromSearches() {
		Item item = newItem("Hidden Item", "PCS");
		itemRepo.persist(item);
		em.flush();

		Long id = item.id;
		itemRepo.softDeleteById(id);
		em.flush();
		em.clear();

		assertTrue(itemRepo.findByNameEqualsIgnoreCase("Hidden Item").isEmpty(),
				"exact-name lookup must hide the soft-deleted item");
		assertTrue(itemRepo.findByItemNumber("Hidden Item").isEmpty(),
				"item-number lookup must hide the soft-deleted item");
		assertEquals(0L, itemRepo.countByNameContainingIgnoreCase("Hidden"),
				"count must exclude the soft-deleted item");
	}
}
