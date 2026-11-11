package id.putra.core_erp.modules.department.repo;

import java.util.List;
import java.util.Optional;

import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.repository.SoftDeleteRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentRepo implements SoftDeleteRepository<Department> {

	public Optional<Department> findByDeptNumber(String deptNumber) {
		return find("deptNumber = ?1", deptNumber).firstResultOptional();
	}

	public List<Department> findAllByName(String deptName, Page page, Sort sort) {
		return find("deptName like ?1% ", sort, deptName).page(page).list();
	}

}
