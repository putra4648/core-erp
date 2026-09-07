package id.putra.core_erp.purchase_requisition.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import id.putra.core_erp.purchase_requisition.constant.MessageConstant;
import id.putra.core_erp.purchase_requisition.dto.ApprovalDto;
import id.putra.core_erp.purchase_requisition.dto.DepartmentDto;
import id.putra.core_erp.purchase_requisition.dto.UsersDto;
import id.putra.core_erp.purchase_requisition.enums.StatusEnum;
import id.putra.core_erp.purchase_requisition.model.Approval;
import id.putra.core_erp.purchase_requisition.model.Department;
import id.putra.core_erp.purchase_requisition.model.Users;
import id.putra.core_erp.purchase_requisition.repository.DepartmentRepo;
import id.putra.core_erp.purchase_requisition.service.DepartmentsService;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DepartmentsServiceImpl implements DepartmentsService {

    @Inject
    private DepartmentRepo departmentRepo;

    @Override
    public List<DepartmentDto> getDepartmentDropdown() {
        List<DepartmentDto> dtos = new ArrayList<>();
        departmentRepo.findAll().list().forEach(data -> dtos.add(entityToDto(data)));
        return dtos;
    }

    @Override
    public Map<String, Object> getDepartments(String type, String name) {
        Map<String, Object> detail = new HashMap<>();

        if (type.equalsIgnoreCase("status")) {
            if (name == null) {
                name = "";
            }
            for (StatusEnum status : StatusEnum.values()) {
                Map<String, Object> detailResult = new HashMap<>();
                detailResult.put("count", departmentRepo.countByDeptNameContainingIgnoreCaseAndStatus(name, status));
                List<DepartmentDto> depts = new ArrayList<>();
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

    @Override
    public void addDepartment(DepartmentDto body) {
        Optional<Department> dept = departmentRepo.findByIdOptional(body.getId());
        if (dept.isPresent()) {
            String message = MessageFormat.format(MessageConstant.EXIST, "Department ", body.getDept_number());
            throw new IllegalArgumentException(message);
        } else {
            Department entity = new Department();
            departmentRepo.persist(dtoToEntity(entity, body));
        }
    }

    @Override
    public DepartmentDto getDetailDepartment(Long id) {
        Optional<Department> deptOptional = departmentRepo.findByIdOptional(id);
        if (deptOptional.isPresent()) {
            Department dept = deptOptional.get();
            return entityToDto(dept);
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Department " + id);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void updateDepartment(DepartmentDto body) {
        Optional<Department> dept = departmentRepo.findByDeptNumber(body.getDept_number());
        if (dept.isPresent()) {
            Department updatedEntity = dtoToEntity(dept.get(), body);
            departmentRepo.persist(updatedEntity);
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Department " + body.getDept_number());
            throw new IllegalArgumentException(message);
        }
    }

    @Override
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

    private DepartmentDto entityToDto(Department data) {
        DepartmentDto dto = new DepartmentDto();
        dto.setDept_name(data.getDeptName());
        dto.setDept_number(data.getDeptNumber());
        dto.setStatus(data.getStatus());

        UsersDto user = new UsersDto();

        if (data.getDeptManager() != null) {
            user.setNrp(data.getDeptManager().getNrp());
            user.setFullname(data.getDeptManager().getFullname());
            user.setStatus(data.getDeptManager().getStatus());
            user.setUsername(data.getDeptManager().getUsername());
            dto.setDept_manager(user);
        }

        List<ApprovalDto> approvals = new ArrayList<>();
        if (data.getApprovals() != null) {
            data.getApprovals().forEach(aprv -> {
                ApprovalDto approvalDto = new ApprovalDto();
                approvalDto.setId(aprv.getId());

                if (aprv.getUsers() != null) {
                    UsersDto user1 = new UsersDto();
                    user1.setNrp(aprv.getUsers().getNrp());
                    user1.setFullname(aprv.getUsers().getFullname());
                    user1.setStatus(aprv.getUsers().getStatus());
                    user1.setUsername(aprv.getUsers().getUsername());
                    approvalDto.setUsers(user1);
                }
                approvals.add(approvalDto);
            });
        }
        dto.setApprovals(approvals);

        if (data.getDeptPurchaser() != null) {
            user = new UsersDto();
            user.setNrp(data.getDeptPurchaser().getNrp());
            user.setFullname(data.getDeptPurchaser().getFullname());
            user.setStatus(data.getDeptPurchaser().getStatus());
            user.setUsername(data.getDeptPurchaser().getUsername());
            dto.setDept_purchaser(user);
        }

        if (data.getApprovalPresident() != null) {
            user = new UsersDto();
            user.setNrp(data.getApprovalPresident().getNrp());
            user.setFullname(data.getApprovalPresident().getFullname());
            user.setStatus(data.getApprovalPresident().getStatus());
            user.setUsername(data.getApprovalPresident().getUsername());
            dto.setApproval_president(user);
        }

        return dto;
    }

    private Department dtoToEntity(Department entity, DepartmentDto data) {
        entity.setDeptName(data.getDept_name());
        entity.setDeptNumber(data.getDept_number());
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
            for (ApprovalDto approvalDto : data.getApprovals()) {
                if (approvalDto.getUsers() != null && approvalDto.getUsers().getNrp() != null) {
                    Approval approval = new Approval();
                    // Optional<Users> user1 = userRepo.findById(approvalDto.getUsers().getNrp());
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

    @Override
    public List<ApprovalDto> getDeptApprovals(Long deptId) {
        List<ApprovalDto> dtos = new ArrayList<>();

        departmentRepo.findByIdOptional(deptId).ifPresent(value -> {
            if (value.getApprovals() != null) {
                dtos.addAll(value.getApprovals().stream().map(apr -> {
                    ApprovalDto dto = new ApprovalDto();
                    dto.setUsers(entityToUsersDto(apr.getUsers()));

                    /// Set default setting
                    dto.setIsApprovalChecked(true);
                    dto.setIsApprovalPermanent(false);
                    dto.setIsBypassChecked(false);
                    dto.setIsBypassPermanent(false);
                    return dto;
                }).toList());
            }
        });

        return dtos;
    }

    @Override
    public ApprovalDto getDeptApprovalPurchaser(Long deptId) {
        ApprovalDto dto = null;
        Optional<Department> deptOpt = departmentRepo.findByIdOptional(deptId);
        if (deptOpt.isPresent()) {
            dto = new ApprovalDto();
            Department dept = deptOpt.get();

            Users deptPurchaserUser = dept.getDeptPurchaser();
            if (deptPurchaserUser != null) {
                dto.setUsers(entityToUsersDto(deptPurchaserUser));
            }

            /// Set default setting
            dto.setIsApprovalChecked(true);
            dto.setIsApprovalPermanent(false);
            dto.setIsBypassChecked(false);
            dto.setIsBypassPermanent(false);
        }
        return dto;
    }

    private UsersDto entityToUsersDto(Users users) {
        if (users == null)
            return null;
        UsersDto dto = new UsersDto();
        dto.setNrp(users.getNrp());
        dto.setFullname(users.getFullname());
        return dto;
    }

}
