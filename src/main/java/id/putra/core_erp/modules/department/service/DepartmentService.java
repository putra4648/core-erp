package id.putra.core_erp.modules.department.service;

<<<<<<< HEAD
import java.util.List;
import java.util.Optional;

=======
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import id.putra.core_erp.modules.approval.model.Approval;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
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
<<<<<<< HEAD
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
=======
	public void save(Department entity) {
		departmentRepo.persist(entity);
	}

	@Override
	public void update(Department entity) {
		departmentRepo.getEntityManager().merge(entity);
	}

	public List<Approval> getDeptApprovals(Long id) {
		List<Approval> dtos = new ArrayList<>();

		departmentRepo.findByIdOptional(id).ifPresent(value -> {
			if (value.getApprovals() != null) {
				dtos.addAll(value.getApprovals().stream().map(apr -> {
					Approval dto = new Approval();
					// dto.setUsers(apr.getUsers());

					/// Set default setting
					// dto.setIsApprovalChecked(true);
					// dto.setIsApprovalPermanent(false);
					// dto.setIsBypassChecked(false);
					// dto.setIsBypassPermanent(false);
					return dto;
				}).toList());
			}
		});

		return dtos;
	}

	public Approval getDeptApprovalPurchaser(Long deptId) {
		Approval dto = null;
		Optional<Department> deptOpt = departmentRepo.findByIdOptional(deptId);
		if (deptOpt.isPresent()) {
			dto = new Approval();
			Department dept = deptOpt.get();

			String deptPurchaserUser = dept.getDeptPurchaser();
			if (deptPurchaserUser != null) {
				dto.setUsers(deptPurchaserUser);
			}

			/// Set default setting
			// dto.setIsApprovalChecked(true);
			// dto.setIsApprovalPermanent(false);
			// dto.setIsBypassChecked(false);
			// dto.setIsBypassPermanent(false);
		}
		return dto;
	}

	@Override
	public Department findById(Long id) {
		return departmentRepo.findById(id);
	}

	@Override
	public void delete(Long id) {
		departmentRepo.deleteById(id);
	}

	@Override
	public Iterable<Department> findAll(Department d, Page page) {
		return departmentRepo.find("name like ?1", "%" + d.getName() + "%").page(page).list();
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

}
