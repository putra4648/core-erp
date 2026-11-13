package id.putra.core_erp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.modules.warehouse.model.Rack;
import id.putra.core_erp.modules.warehouse.model.Warehouse;
import id.putra.core_erp.modules.warehouse.model.Zone;
import id.putra.core_erp.modules.warehouse.repository.RackRepository;
import id.putra.core_erp.modules.warehouse.repository.WarehouseRepository;
import id.putra.core_erp.modules.warehouse.repository.ZoneRepository;
import id.putra.core_erp.support.TestFixtures;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers the storage-hierarchy cascade: {@code Warehouse → Zone → Rack}.
 *
 * <p>
 * {@code SoftDeleteTest} already asserts the chain end to end. What it does not
 * cover is
 * the leaf: dropping the last rack must be self-contained, and a rack under a
 * *different*
 * zone must survive another zone's delete. Those are the two ways a recursive
 * cascade goes
 * wrong — either stopping early or overshooting into siblings.
 */
@QuarkusTest
class WarehouseCascadeRepoTest {

	@Inject
	WarehouseRepository warehouseRepository;

	@Inject
	ZoneRepository zoneRepository;

	@Inject
	RackRepository rackRepository;

	@Inject
	EntityManager em;

	@Test
	@TestTransaction
	@DisplayName("soft-deleting a rack does not touch its zone or warehouse")
	void deletingLeafDoesNotTouchParents() {
		Warehouse wh = TestFixtures.warehouse("WH-Leaf");
		warehouseRepository.persist(wh);
		Zone zone = TestFixtures.zone("Z-Leaf", wh);
		zoneRepository.persist(zone);
		Rack rack = TestFixtures.rack("R-Leaf", zone);
		rackRepository.persist(rack);
		em.flush();

		Long whId = wh.id;
		Long zoneId = zone.id;
		Long rackId = rack.id;

		rackRepository.softDeleteById(rackId);
		em.flush();
		em.clear();

		assertNull(rackRepository.findById(rackId), "rack must be hidden");
		// Cascade runs downward only — parents must be untouched.
		assertEquals(whId, warehouseRepository.findById(whId).id,
				"the warehouse must survive a child delete");
		assertEquals(zoneId, zoneRepository.findById(zoneId).id,
				"the zone must survive a child delete");
	}

	@Test
	@TestTransaction
	@DisplayName("soft-deleting a zone cascades to its own racks and no others")
	void zoneDeleteScopesToItsOwnRacks() {
		Warehouse wh = TestFixtures.warehouse("WH-Scope");
		warehouseRepository.persist(wh);

		Zone target = TestFixtures.zone("Z-Target", wh);
		zoneRepository.persist(target);
		Zone sibling = TestFixtures.zone("Z-Sibling", wh);
		zoneRepository.persist(sibling);

		Rack targetRack = TestFixtures.rack("R-In-Target", target);
		rackRepository.persist(targetRack);
		Rack siblingRack = TestFixtures.rack("R-In-Sibling", sibling);
		rackRepository.persist(siblingRack);
		em.flush();

		Long targetRackId = targetRack.id;
		Long siblingRackId = siblingRack.id;

		zoneRepository.softDeleteById(target.id);
		em.flush();
		em.clear();

		assertNull(rackRepository.findById(targetRackId),
				"the rack inside the deleted zone must cascade");
		assertEquals(siblingRackId, rackRepository.findById(siblingRackId).id,
				"a rack in a sibling zone must NOT be touched — the cascade must not overshoot");
	}

	@Test
	@TestTransaction
	@DisplayName("soft-deleting a warehouse reaches racks two levels down")
	void warehouseDeleteReachesRacks() {
		Warehouse wh = TestFixtures.warehouse("WH-Deep");
		warehouseRepository.persist(wh);
		Zone zone = TestFixtures.zone("Z-Deep", wh);
		zoneRepository.persist(zone);
		Rack rack = TestFixtures.rack("R-Deep", zone);
		rackRepository.persist(rack);
		em.flush();

		Long whId = wh.id;
		Long zoneId = zone.id;
		Long rackId = rack.id;

		warehouseRepository.softDeleteById(whId);
		em.flush();
		em.clear();

		assertNull(warehouseRepository.findById(whId), "warehouse must be hidden");
		assertNull(zoneRepository.findById(zoneId), "zone must cascade one level");
		assertNull(rackRepository.findById(rackId), "rack must cascade two levels");

		// All three rows must physically remain, flagged.
		assertEquals(Boolean.TRUE, em.createNativeQuery("select deleted from warehouse where id = ?1")
				.setParameter(1, whId).getSingleResult());
		assertEquals(Boolean.TRUE, em.createNativeQuery("select deleted from zone where id = ?1")
				.setParameter(1, zoneId).getSingleResult());
		// Rack carries an explicit @Table(name = "warehouse_racks") — not the derived name.
		assertEquals(Boolean.TRUE, em.createNativeQuery("select deleted from warehouse_racks where id = ?1")
				.setParameter(1, rackId).getSingleResult());
	}

	@Test
	@TestTransaction
	@DisplayName("a warehouse with no children deletes cleanly")
	void warehouseWithoutChildrenDeletesCleanly() {
		Warehouse wh = TestFixtures.warehouse("WH-Empty");
		warehouseRepository.persist(wh);
		em.flush();

		Long id = wh.id;
		warehouseRepository.softDeleteById(id);
		em.flush();
		em.clear();

		assertNull(warehouseRepository.findById(id),
				"an empty warehouse must soft-delete without the cascade needing anything to walk");
		assertEquals(1, warehouseRepository.restore(id), "and must be restorable");
	}
}
