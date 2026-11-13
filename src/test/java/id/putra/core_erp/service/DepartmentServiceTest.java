package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.modules.department.service.DepartmentService;
import id.putra.core_erp.support.ServiceDelegationAssertions;
import id.putra.core_erp.support.TestFixtures;

/**
 * Plain JUnit + Mockito: {@link DepartmentService} carries no business rules,
 * so there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

	@Mock
	DepartmentRepo departmentRepo;

	@InjectMocks
	DepartmentService departmentService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		Department entity = TestFixtures.department("DPT-1");

		departmentService.save(entity);

		verify(departmentRepo).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		departmentService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(departmentRepo, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		Department entity = TestFixtures.department("DPT-1");
		given(departmentRepo.findById(3L)).willReturn(entity);

		assertSame(entity, departmentService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(departmentRepo.count()).willReturn(42L);

		assertSame(42L, departmentService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(departmentRepo);
	}
}
