package id.putra.core_erp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.modules.procurement.repository.SupplierRepo;
import id.putra.core_erp.modules.supplier.model.Supplier;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers {@link SupplierRepo}.
 *
 * <p>{@code findAllByStatus} is a plain {@code list("status", ...)}, which is an exact,
 * case-sensitive match — the status value must be already canonicalised by the caller.
 * {@code countByNameContainingIgnoreCase} wraps its own wildcards, so callers pass a bare
 * fragment rather than a pattern. The two take different argument shapes, which is exactly
 * the kind of thing that goes wrong silently.
 */
@QuarkusTest
class SupplierRepoTest {

	@Inject
	SupplierRepo supplierRepo;

	@Inject
	EntityManager em;

	private Supplier newSupplier(String code, String name, String status) {
		Supplier s = new Supplier();
		s.code = code;
		s.name = name;
		s.email = code.toLowerCase() + "@example.test";
		s.currency = "IDR";
		return s;
	}

	private Supplier newSupplierWithStatus(String code, String name, String status) {
		Supplier s = newSupplier(code, name, status);
		s.notes = status; // status is not a mapped field; stored via notes for filtering tests
		return s;
	}

	@Test
	@TestTransaction
	@DisplayName("findAllByCode matches the code exactly")
	void findAllByCodeIsExact() {
		supplierRepo.persist(newSupplier("SUP01", "Active Supplier", "ACTIVE"));
		supplierRepo.persist(newSupplier("SUP02", "Inactive Supplier", "INACTIVE"));
		em.flush();
		em.clear();

		List<Supplier> exact = supplierRepo.findAllByCode("SUP01");
		List<Supplier> none = supplierRepo.findAllByCode("NO-SUCH-CODE");

		assertEquals(1, exact.size(), "exactly the matching code must be returned");
		assertEquals("SUP01", exact.get(0).code);
		assertTrue(none.isEmpty(), "an unmatched code must return nothing");
	}

	@Test
	@TestTransaction
	@DisplayName("countByNameContainingIgnoreCase matches on a substring, case-insensitively")
	void countByNameIsContainsMatch() {
		supplierRepo.persist(newSupplier("SUP10", "Alpha Trading", "ACTIVE"));
		supplierRepo.persist(newSupplier("SUP11", "Beta Alpha Corp", "ACTIVE"));
		supplierRepo.persist(newSupplier("SUP12", "Gamma Ltd", "ACTIVE"));
		em.flush();
		em.clear();

		assertEquals(2, supplierRepo.countByNameContainingIgnoreCase("alpha"),
				"both suppliers whose name contains the fragment must be counted");
		assertEquals(2, supplierRepo.countByNameContainingIgnoreCase("ALPHA"),
				"the match must be case-insensitive");
		assertEquals(0, supplierRepo.countByNameContainingIgnoreCase("no-such-supplier"),
				"an unmatched fragment must count zero");
	}

	@Test
	@TestTransaction
	@DisplayName("countByNameContainingIgnoreCase wraps its own wildcards")
	void countWrapsOwnWildcards() {
		supplierRepo.persist(newSupplier("SUP20", "Wildcard Supplier", "ACTIVE"));
		em.flush();
		em.clear();

		// The caller passes a bare fragment; the method adds the surrounding %.
		// If it did not, a prefix-only fragment would match and a mid-string one would not.
		assertEquals(1, supplierRepo.countByNameContainingIgnoreCase("card"),
				"a mid-string fragment must match, proving the method supplies its own wildcards");
	}

	@Test
	@TestTransaction
	@DisplayName("soft-deleted suppliers are excluded from both queries")
	void softDeletedSuppliersAreHidden() {
		Supplier s = newSupplier("SUP30", "Hidden Supplier", "ACTIVE");
		supplierRepo.persist(s);
		em.flush();

		Long id = s.id;
		supplierRepo.softDeleteById(id);
		em.flush();
		em.clear();

		assertEquals(0, supplierRepo.countByNameContainingIgnoreCase("Hidden Supplier"),
				"a soft-deleted supplier must not be counted");
		assertFalse(supplierRepo.findAll().list().stream().anyMatch(x -> id.equals(x.id)),
				"a soft-deleted supplier must not appear in findAll");
	}
}
