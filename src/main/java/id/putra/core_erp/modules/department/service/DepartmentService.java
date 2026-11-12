package id.putra.core_erp.modules.department.service;

import java.util.List;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class DepartmentService implements BaseService<Department> {

	private final DepartmentRepo departmentRepo;

	public DepartmentService(DepartmentRepo departmentRepo) {
		this.departmentRepo = departmentRepo;
	}

	@Override
	public void save(Department entity) {
		departmentRepo.persist(entity);
	}

	@Override
	public void update(Department entity) {
		departmentRepo.getEntityManager().merge(entity);
	}

	@Override
	public Department findById(Long id) {
		return departmentRepo.findById(id);
	}

	@Override
	public void delete(Long id) {
		departmentRepo.softDeleteById(id);
	}

	@Override
	public List<Department> findAll(Department d, Page page) {
		return departmentRepo.findAll().page(page).list();
	}

	@Override
	public long count() {
		return departmentRepo.count();
	}
}
