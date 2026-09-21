package id.putra.core_erp.modules.department.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import id.putra.core_erp.constant.MessageConstant;
import id.putra.core_erp.enums.StatusEnum;
import id.putra.core_erp.modules.approval.model.Approval;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentsService {

    private final DepartmentRepo departmentRepo;

    public DepartmentsService(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    public List<Department> getDepartmentDropdown() {
        List<Department> dtos = new ArrayList<>();
        departmentRepo.findAll().list().forEach(data -> dtos.add(entityToDto(data)));
        return dtos;
    }

    public Map<String, Object> getDepartments(String type, String name) {
        Map<String, Object> detail = new HashMap<>();

        if (type.equalsIgnoreCase("status")) {
            if (name == null) {
                name = "";
            }
            for (StatusEnum status : StatusEnum.values()) {
                Map<String, Object> detailResult = new HashMap<>();
                detailResult.put("count", departmentRepo.countByDeptNameContainingIgnoreCaseAndStatus(name, status));
                List<Department> depts = new ArrayList<>();
                departmentRepo
                        .findAllByDeptNameContainingIgnoreCaseAndStatus(name, status, Sort.by("deptName").ascending())
                        .iterator()
                        .forEachRemaining(data -> depts.add(entityToDto(data)));
                detailResult.put("results", depts);
                detail.put(status.getValue(), detailResult);
            }
            return detail;
        }
        String message = MessageFormat.format(MessageConstant.NOT_FOUND, type);
        throw new IllegalArgumentException(message);
    }

    public void addDepartment(Department body) {
        Optional<Department> dept = departmentRepo.findByIdOptional(body.getId());
        if (dept.isPresent()) {
            String message = MessageFormat.format(MessageConstant.EXIST, "Department ", body.getDeptNumber());
            throw new IllegalArgumentException(message);
        } else {
            Department entity = new Department();
            departmentRepo.persist(dtoToEntity(entity, body));
        }
    }

    public Department getDetailDepartment(Long id) {
        Optional<Department> deptOptional = departmentRepo.findByIdOptional(id);
        if (deptOptional.isPresent()) {
            Department dept = deptOptional.get();
            return entityToDto(dept);
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Department " + id);
            throw new IllegalArgumentException(message);
        }
    }

    public void updateDepartment(Department body) {
        Optional<Department> dept = departmentRepo.findByDeptNumber(body.getDeptNumber());
        if (dept.isPresent()) {
            Department updatedEntity = dtoToEntity(dept.get(), body);
            departmentRepo.persist(updatedEntity);
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Department " + body.getDeptNumber());
            throw new IllegalArgumentException(message);
        }
    }

    public void updateDepartmentStatus(Long deptId) {
        Optional<Department> dept = departmentRepo.findByIdOptional(deptId);
        if (dept.isPresent()) {
            Department entity = dept.get();
            entity.setStatus(entity.getStatus() == StatusEnum.ACTIVE ? StatusEnum.INACTIVE : StatusEnum.ACTIVE);
            departmentRepo.persist(entity);
        } else {
            String message = MessageFormat.format(MessageConstant.UPDATE_STATUS, "department ", deptId);
            throw new IllegalArgumentException(message);
        }
    }

    private Department entityToDto(Department data) {
        Department dto = new Department();
        dto.setDeptName(data.getDeptName());
        dto.setDeptNumber(data.getDeptNumber());
        dto.setStatus(data.getStatus());

        // Users user = new Users();

        // if (data.getDeptManager() != null) {
        // user.setNrp(data.getDeptManager().getNrp());
        // user.setFullname(data.getDeptManager().getFullname());
        // user.setStatus(data.getDeptManager().getStatus());
        // user.setUsername(data.getDeptManager().getUsername());
        // dto.setDeptManager(user);
        // }

        List<Approval> approvals = new ArrayList<>();
        if (data.getApprovals() != null) {
            data.getApprovals().forEach(aprv -> {
                Approval Approval = new Approval();
                Approval.setId(aprv.getId());

                // if (aprv.getUsers() != null) {
                // Users user1 = new Users();
                // user1.setNrp(aprv.getUsers().getNrp());
                // user1.setFullname(aprv.getUsers().getFullname());
                // user1.setStatus(aprv.getUsers().getStatus());
                // user1.setUsername(aprv.getUsers().getUsername());
                // Approval.setUsers(user1);
                // }
                approvals.add(Approval);
            });
        }
        dto.setApprovals(approvals);

        // if (data.getDeptPurchaser() != null) {
        // user = new Users();
        // user.setNrp(data.getDeptPurchaser().getNrp());
        // user.setFullname(data.getDeptPurchaser().getFullname());
        // user.setStatus(data.getDeptPurchaser().getStatus());
        // user.setUsername(data.getDeptPurchaser().getUsername());
        // dto.setDeptPurchaser(user);
        // }

        // if (data.getApprovalPresident() != null) {
        // user = new Users();
        // user.setNrp(data.getApprovalPresident().getNrp());
        // user.setFullname(data.getApprovalPresident().getFullname());
        // user.setStatus(data.getApprovalPresident().getStatus());
        // user.setUsername(data.getApprovalPresident().getUsername());
        // dto.setApprovalPresident(user);
        // }

        return dto;
    }

    private Department dtoToEntity(Department entity, Department data) {
        entity.setDeptName(data.getDeptName());
        entity.setDeptNumber(data.getDeptNumber());
        entity.setStatus(data.getStatus());

        // if (data.getDept_manager() != null && data.getDept_manager().getNrp() !=
        // null) {
        // Optional<Users> user = userRepo.findById(data.getDept_manager().getNrp());
        // user.ifPresent(entity::setDeptManager);
        // }

        // if (data.getDept_purchaser() != null && data.getDept_purchaser().getNrp() !=
        // null) {
        // Optional<Users> user = userRepo.findById(data.getDept_purchaser().getNrp());
        // user.ifPresent(entity::setDeptPurchaser);
        // }

        // if (data.getApproval_president() != null &&
        // data.getApproval_president().getNrp() != null) {
        // Optional<Users> user =
        // userRepo.findById(data.getApproval_president().getNrp());
        // user.ifPresent(entity::setApprovalPresident);
        // }

        List<Approval> approvals = entity.getApprovals();
        if (approvals != null) {
            approvals.clear();
        } else {
            approvals = new ArrayList<>();
        }

        if (data.getApprovals() != null) {
            for (Approval Approval : data.getApprovals()) {
                if (Approval.getUsers() != null && Approval.getUsers() != null) {
                    // Approval approval = new Approval();
                    // Optional<Users> user1 = userRepo.findById(Approval.getUsers().getNrp());
                    // if (user1.isPresent()) {
                    // approval.setDepartment(entity);
                    // approval.setUsers(user1.get());
                    // approvals.add(approval);
                    // }
                }
            }
        }
        entity.setApprovals(approvals);
        return entity;
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

}
