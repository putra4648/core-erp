package id.putra.core_erp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import id.putra.core_erp.modules.customer.model.Customer;
import id.putra.core_erp.modules.customer.repository.CustomerRepository;
import id.putra.core_erp.modules.warehouse.model.Rack;
import id.putra.core_erp.modules.warehouse.model.Warehouse;
import id.putra.core_erp.modules.warehouse.model.Zone;
import id.putra.core_erp.modules.warehouse.repository.RackRepository;
import id.putra.core_erp.modules.warehouse.repository.WarehouseRepository;
import id.putra.core_erp.modules.warehouse.repository.ZoneRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Verifies the soft delete contract end to end: the flag hides rows from JPA queries,
 * restore brings them back, and cascade walks the object graph.
 *
 * <p>Every test runs inside a rolled-back transaction ({@link TestTransaction}), so no
 * state leaks between tests and the developer database is never polluted.
 */
@QuarkusTest
class SoftDeleteTest {

	@Inject
	CustomerRepository customerRepository;

	@Inject
	WarehouseRepository warehouseRepository;

	@Inject
	ZoneRepository zoneRepository;

	@Inject
	RackRepository rackRepository;

	@Inject
	EntityManager em;

	private Customer newCustomer(String name) {
		Customer c = new Customer();
		c.name = name;
		c.email = name.toLowerCase() + "@example.test";
		customerRepository.persist(c);
		return c;
	}

	@Test
	@TestTransaction
	void softDeleteHidesRowFromFindByIdAndFindAll() {
		Customer c = newCustomer("SoftDeleteFind");
		Long id = c.id;
		assertNotNull(customerRepository.findById(id), "entity should be visible before delete");

		customerRepository.softDeleteById(id);
		em.flush();
		em.clear();

		assertNull(customerRepository.findById(id), "findById must not return a soft-deleted row");
		assertFalse(customerRepository.findAll().list().stream().anyMatch(x -> id.equals(x.id)),
				"findAll must not return a soft-deleted row");
		assertEquals(0, customerRepository.count("id", id),
				"count must not include a soft-deleted row");
	}

	@Test
	@TestTransaction
	void softDeleteKeepsPhysicalRow() {
		Customer c = newCustomer("SoftDeletePhysical");
		Long id = c.id;
		customerRepository.softDeleteById(id);
		em.flush();
		em.clear();

		// Native query bypasses @SQLRestriction on purpose: the row must still exist.
		Object flag = em.createNativeQuery("select deleted from customer where id = ?1")
				.setParameter(1, id)
				.getSingleResult();
		assertEquals(Boolean.TRUE, flag, "row must remain in the table with deleted = true");
	}

	@Test
	@TestTransaction
	void restoreBringsRowBack() {
		Customer c = newCustomer("SoftDeleteRestore");
		Long id = c.id;
		customerRepository.softDeleteById(id);
		em.flush();
		em.clear();
		assertNull(customerRepository.findById(id));

		int updated = customerRepository.restore(id);
		em.flush();
		em.clear();

		assertEquals(1, updated, "restore must update exactly one row");
		Customer restored = customerRepository.findById(id);
		assertNotNull(restored, "restored row must be visible again");
		assertFalse(restored.deleted);
		assertNull(restored.deletedAt, "restore must clear deletedAt");
		assertNull(restored.deletedBy, "restore must clear deletedBy");
	}

	@Test
	@TestTransaction
	void findAllIncludingDeletedSeesFlaggedRows() {
		Customer c = newCustomer("SoftDeleteIncluding");
		Long id = c.id;
		customerRepository.softDeleteById(id);
		em.flush();
		em.clear();

		List<Customer> all = customerRepository.findAllIncludingDeleted();
		assertTrue(all.stream().anyMatch(x -> id.equals(x.id)),
				"findAllIncludingDeleted must include soft-deleted rows");
	}

	@Test
	@TestTransaction
	void cascadeWalksWarehouseZoneRack() {
		Warehouse warehouse = new Warehouse();
		warehouse.name = "CascadeWH";
		warehouse.location = "Test";
		warehouseRepository.persist(warehouse);

		Zone zone = new Zone();
		zone.code = "CSC";
		zone.name = "CascadeZone";
		zone.warehouse = warehouse;
		zoneRepository.persist(zone);

		Rack rack = new Rack();
		rack.name = "CascadeRack";
		rack.rows = 1;
		rack.cols = 1;
		rack.zone = zone;
		rackRepository.persist(rack);

		Long whId = warehouse.id;
		Long zoneId = zone.id;
		Long rackId = rack.id;
		em.flush();
		em.clear();

		warehouseRepository.softDeleteById(whId);
		em.flush();
		em.clear();

		assertNull(warehouseRepository.findById(whId), "warehouse must be hidden");
		assertNull(zoneRepository.findById(zoneId), "zone must cascade to deleted");
		assertNull(rackRepository.findById(rackId), "rack must cascade two levels down");

		Object rackFlag = em.createNativeQuery("select deleted from zone where id = ?1")
				.setParameter(1, zoneId).getSingleResult();
		assertEquals(Boolean.TRUE, rackFlag, "cascaded zone row must exist with deleted = true");
	}

	@Test
	@TestTransaction
	void softDeleteIsIdempotent() {
		Customer c = newCustomer("SoftDeleteTwice");
		Long id = c.id;

		customerRepository.softDeleteById(id);
		em.flush();
		// Second call must be a silent no-op: the row is already hidden from findByIdOptional.
		customerRepository.softDeleteById(id);
		em.flush();
		em.clear();

		assertNull(customerRepository.findById(id));
		assertEquals(1, customerRepository.restore(id), "flag must still be flipped exactly once");
	}
}
