package id.putra.core_erp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.modules.procurement.model.Transfer;
import id.putra.core_erp.modules.procurement.repository.TransferRequestRepo;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers {@link TransferRequestRepo}.
 *
 * <p>
 * {@code findByStatusLikeAndIdLike} is driven by {@code TransferService}, which
 * passes
 * {@code "%%"} for a blank filter. That means the {@code %%} fallback — not the
 * repository —
 * is what makes an empty search return everything, so these tests pin down the
 * pattern
 * semantics the service relies on.
 */
@QuarkusTest
class TransferRequestRepoTest {

	@Inject
	TransferRequestRepo transferRequestRepo;

	@Inject
	EntityManager em;

	private Transfer newTransfer(String title, String status) {
		Transfer t = new Transfer();
		t.title = title;
		t.status = status;
		t.dueDate = LocalDate.now().plusDays(14);
		t.note = "fixture";
		return t;
	}

	@Test
	@TestTransaction
	@DisplayName("a double-wildcard status matches every row, which is the blank-filter fallback")
	void doubleWildcardMatchesAll() {
		transferRequestRepo.persist(newTransfer("TRF-All-1", "DRAFT"));
		transferRequestRepo.persist(newTransfer("TRF-All-2", "SENT"));
		em.flush();
		em.clear();

		List<Transfer> hits = transferRequestRepo.findByStatusLikeAndIdLike("%%", null);

		assertTrue(hits.size() >= 2,
				"%% must match every row — this is what TransferService relies on for a blank search");
	}

	@Test
	@TestTransaction
	@DisplayName("the status pattern filters the result")
	void statusPatternFilters() {
		transferRequestRepo.persist(newTransfer("TRF-Filter-1", "ONLY-DRAFT"));
		transferRequestRepo.persist(newTransfer("TRF-Filter-2", "ONLY-SENT"));
		em.flush();
		em.clear();

		List<Transfer> hits = transferRequestRepo.findByStatusLikeAndIdLike("ONLY-DRAFT", null);

		assertEquals(1, hits.size(), "only the matching status must be returned");
		assertEquals("ONLY-DRAFT", hits.get(0).status);
	}

	@Test
	@TestTransaction
	@DisplayName("like semantics mean a bare fragment matches partially, unlike an exact filter")
	void likeIsPartialMatch() {
		transferRequestRepo.persist(newTransfer("TRF-Like-1", "PARTIAL-STATUS"));
		em.flush();
		em.clear();

		List<Transfer> hits = transferRequestRepo.findByStatusLikeAndIdLike("PARTIAL%", null);

		assertEquals(1, hits.size(), "a prefix pattern must match — the operand is like, not equality");
	}

	@Test
	@TestTransaction
	@DisplayName("findByTitle resolves a single transfer")
	void findByTitleResolves() {
		transferRequestRepo.persist(newTransfer("Unique Transfer Title", "DRAFT"));
		em.flush();
		em.clear();

		assertTrue(transferRequestRepo.findByTitle("Unique Transfer Title").isPresent(),
				"the transfer must be found by its title");
		assertTrue(transferRequestRepo.findByTitle("No Such Transfer").isEmpty());
	}

	@Test
	@TestTransaction
	@DisplayName("soft-deleted transfers are invisible to both queries")
	void softDeletedTransfersAreHidden() {
		Transfer t = newTransfer("Deleted Transfer", "DRAFT");
		transferRequestRepo.persist(t);
		em.flush();

		Long id = t.id;
		transferRequestRepo.softDeleteById(id);
		em.flush();
		em.clear();

		assertFalse(transferRequestRepo.findByStatusLikeAndIdLike("%%", null).stream()
				.anyMatch(x -> id.equals(x.id)),
				"a soft-deleted transfer must not appear in the wildcard search");
		assertTrue(transferRequestRepo.findByTitle("Deleted Transfer").isEmpty(),
				"a soft-deleted transfer must not be found by title");
	}
}
