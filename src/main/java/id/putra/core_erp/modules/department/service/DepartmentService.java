package id.putra.core_erp.modules.department.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import id.putra.core_erp.constant.MessageConstant;
import id.putra.core_erp.modules.approval.model.Approval;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentService implements BaseService<Department> {

    private final DepartmentRepo departmentRepo;

    public DepartmentService(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    @Override
    public void save(Department body) {
        Optional<Department> dept = departmentRepo.findByIdOptional(body.getId());
        if (dept.isPresent()) {
            String message = MessageFormat.format(MessageConstant.EXIST, "Department ", body.getCode());
            throw new IllegalArgumentException(message);
        } else {
            departmentRepo.persist(body);
        }
    }

    public Department getDetailDepartment(Long id) {
        Optional<Department> deptOptional = departmentRepo.findByIdOptional(id);
        if (deptOptional.isPresent()) {
            return deptOptional.get();
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Department " + id);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void update(Department body) {
        Optional<Department> dept = departmentRepo.findByDeptNumber(body.getCode());
        if (dept.isPresent()) {
            departmentRepo.persist(body);
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Department " + body.getCode());
            throw new IllegalArgumentException(message);
        }
    }

    public List<Approval> getDeptApprovals(Long deptId) {
        List<Approval> dtos = new ArrayList<>();

        departmentRepo.findByIdOptional(deptId).ifPresent(value -> {
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
        return departmentRepo.find("name like ?1", d.getName()).page(page).list();
    }

}
