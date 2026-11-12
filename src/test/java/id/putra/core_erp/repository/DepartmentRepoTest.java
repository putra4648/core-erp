package id.putra.core_erp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.support.TestFixtures;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers the two hand-written queries on {@link DepartmentRepo}.
 *
 * <p>
 * {@code @QuarkusTest} because both are JPQL against real columns — a typo in
 * either
 * only surfaces when Hibernate parses it against the actual metamodel. Plain
 * JUnit could
 * not catch that, which is the whole point of these tests.
 */
@QuarkusTest
class DepartmentRepoTest {

	@Inject
	DepartmentRepo departmentRepo;

	@Inject
	EntityManager em;

	@Test
	@TestTransaction
	@DisplayName("findByDeptNumber matches on the department number, not the surrogate id")
	void findByDeptNumberMatchesCode() {
		Department d = TestFixtures.department("DRT01");
		departmentRepo.persist(d);
		em.flush();

		Optional<Department> found = departmentRepo.findByDeptNumber("DRT01");

		assertTrue(found.isPresent(), "department must be found by its dept_number");
		assertEquals(d.id, found.get().id);

		// The surrogate id must NOT resolve — the query is on deptNumber.
		assertTrue(departmentRepo.findByDeptNumber(String.valueOf(d.id)).isEmpty(),
				"surrogate id must not match the dept_number query");
		assertTrue(departmentRepo.findByDeptNumber("NO-SUCH-DEPT").isEmpty());
	}

	@Test
	@TestTransaction
	@DisplayName("findAllByName uses a prefix match, not a contains match")
	void findAllByNameIsPrefixMatch() {
		Department prefix = TestFixtures.department("PFX01");
		prefix.name = "Warehouse Operations";
		departmentRepo.persist(prefix);

		Department middle = TestFixtures.department("MDL01");
		middle.name = "Central Warehouse Team";
		departmentRepo.persist(middle);
		em.flush();
		em.clear();

		List<Department> hits = departmentRepo.findAllByName("Warehouse%", Page.ofSize(50), Sort.ascending("name"));

		assertTrue(hits.stream().anyMatch(d -> "Warehouse Operations".equals(d.name)),
				"name starting with the term must match");
		assertFalse(hits.stream().anyMatch(d -> "Central Warehouse Team".equals(d.name)),
				"name merely containing the term must NOT match — the caller's pattern controls the match");
	}

	@Test
	@TestTransaction
	@DisplayName("findAllByName honours the page size")
	void findAllByNamePaginates() {
		for (int i = 0; i < 4; i++) {
			Department d = TestFixtures.department("PG" + i);
			d.name = "Paged Department " + i;
			departmentRepo.persist(d);
		}
		em.flush();
		em.clear();

		List<Department> firstPage = departmentRepo.findAllByName("Paged%", Page.ofSize(2), Sort.ascending("name"));

		assertEquals(2, firstPage.size(), "page size must be applied");
	}

	@Test
	@TestTransaction
	@DisplayName("soft-deleted departments are invisible to both queries")
	void softDeletedRowsAreHidden() {
		Department d = TestFixtures.department("DEL01");
		departmentRepo.persist(d);
		em.flush();

		Long id = d.id;
		departmentRepo.softDeleteById(id);
		em.flush();
		em.clear();

		assertNull(departmentRepo.findById(id), "findById must hide the soft-deleted row");
		assertTrue(departmentRepo.findByDeptNumber("DEL01").isEmpty(),
				"findByDeptNumber must hide the soft-deleted row");
		assertFalse(
				departmentRepo.findAllByName("Dept DEL01%", Page.ofSize(50), Sort.ascending("name")).stream()
						.anyMatch(x -> id.equals(x.id)),
				"findAllByName must hide the soft-deleted row");

		// The row itself must still be there with the flag flipped.
		Object flag = em.createNativeQuery("select deleted from department where id = ?1")
				.setParameter(1, id).getSingleResult();
		assertEquals(Boolean.TRUE, flag);
		assertNotNull(departmentRepo.restore(id));
	}
}
