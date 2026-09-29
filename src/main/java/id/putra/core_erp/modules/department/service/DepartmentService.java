package id.putra.core_erp.modules.department.service;

import java.util.List;
import java.util.Optional;

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
	public void delete(Long id) {
		departmentRepo.deleteById(id);
	}

	@Override
	public List<Department> findAll(Department d, Page page) {
		return departmentRepo.find("name like ?1", d.getName()).page(page).list();
	}

	@Override
	public Department findById(Long id) {
		return departmentRepo.findById(id);
	}

	@Override
	public void save(Department body) {
		Optional<Department> dept = departmentRepo.findByIdOptional(body.getId());
		if (dept.isPresent()) {
			String message = String.format("Department %s already exists with code: %s", " ", body.getCode());
			throw new IllegalArgumentException(message);
		} else {
			departmentRepo.persist(body);
		}
	}

	@Override
	public void update(Department entity) {
		departmentRepo.update("name = ?1, where id = ?2", entity.getName(), entity.getId());
	}

}
