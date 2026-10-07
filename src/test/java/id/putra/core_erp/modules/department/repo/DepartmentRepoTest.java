package id.putra.core_erp.modules.department.repo;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import id.putra.core_erp.enums.Status;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.department.model.Department;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class DepartmentRepoTest {

	@InjectMock
	DepartmentRepo departmentRepo;

	@Test
	public void testFindAllByName() throws Throwable {
		var dept1 = new Department();
		dept1.name = "HR";

		var list = new ArrayList<Department>();
		list.add(dept1);

		var page = Page.ofSize(10);
		var sort = Sort.by("name");
		Mockito.when(departmentRepo.findAllByNameAndStatus("HR", Status.ACTIVE, page, sort)).thenReturn(list);
		Assertions.assertEquals(list, departmentRepo.findAllByNameAndStatus("HR", Status.ACTIVE, page, sort));
		Mockito.verify(departmentRepo, Mockito.times(1)).findAllByNameAndStatus("HR", Status.ACTIVE, page,
				sort);
	}

	@Test
	public void testFindByDeptNumber() throws Throwable {
		var dept1 = new Department();
		dept1.name = "HR";
		var budgets = new ArrayList<Budget>();

		var budget = new Budget();
		budget.department = dept1;

		budgets.add(budget);

		// Budget -> Budget Detail -> Budget Allocation
		Mockito.when(departmentRepo.findByDeptNumber("HR")).thenReturn(Optional.of(dept1));
		var result = departmentRepo.findByDeptNumber("HR");
		Assertions.assertTrue(result.isPresent());

		Mockito.verify(departmentRepo, Mockito.times(1)).findByDeptNumber(ArgumentMatchers.anyString());
	}
}
