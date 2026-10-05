package purchase_requisition.repo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;

@QuarkusTest
public class DepartmentRepoTest {

	@InjectMock
	DepartmentRepo departmentRepo;

	@Test
	public void testPanacheRepositoryMocking() throws Throwable {
		// Mocked classes always return a default value
		Assertions.assertEquals(0, departmentRepo.count());

		// Now let's specify the return value
		Mockito.when(departmentRepo.count()).thenReturn(23L);
		Assertions.assertEquals(23, departmentRepo.count());

		// Now let's change the return value
		Mockito.when(departmentRepo.count()).thenReturn(42L);
		Assertions.assertEquals(42, departmentRepo.count());

		// Now let's call the original method
		Mockito.when(departmentRepo.count()).thenCallRealMethod();
		Assertions.assertEquals(0, departmentRepo.count());

		// Check that we called it 4 times
		Mockito.verify(departmentRepo, Mockito.times(4)).count();

		// Mock only with specific parameters
		Department p = new Department();
		Mockito.when(departmentRepo.findById(12L)).thenReturn(p);
		Assertions.assertSame(p, departmentRepo.findById(12L));
		Assertions.assertNull(departmentRepo.findById(42L));

		// Mock throwing
		Mockito.when(departmentRepo.findById(12L)).thenThrow(new WebApplicationException());
		Assertions.assertThrows(WebApplicationException.class, () -> departmentRepo.findById(12L));
	}
}
