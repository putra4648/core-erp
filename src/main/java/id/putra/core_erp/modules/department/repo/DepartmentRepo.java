package id.putra.core_erp.modules.department.repo;

import java.util.List;
import java.util.Optional;

import id.putra.core_erp.enums.Status;
import id.putra.core_erp.modules.department.model.Department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentRepo implements PanacheRepository<Department> {

	public List<Department> findAllByNameAndStatus(String deptName, Status status, Page page, Sort sort) {
		return find("deptName like ?1% and status = ?2 ", sort, deptName, status).page(page).list();
	}

	public Optional<Department> findByDeptNumber(String deptNumber) {
		return find("deptNumber = ?1", deptNumber).firstResultOptional();
	}

}
