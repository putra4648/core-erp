package id.putra.core_erp.purchase_requisition.service.impl;

import java.util.ArrayList;
import java.util.List;

import id.putra.core_erp.purchase_requisition.dto.GroupsDto;
import id.putra.core_erp.purchase_requisition.repository.GroupRepo;
import id.putra.core_erp.purchase_requisition.service.GroupsService;
import io.quarkus.oidc.OidcTenantConfig.Authentication;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GroupServiceImpl implements GroupsService {

    @Inject
    private GroupRepo companyGroupRepo;

    @Override
    public List<GroupsDto> getGroups() {
        List<GroupsDto> dtos = new ArrayList<>();
        companyGroupRepo.findAll().list().forEach(data -> {
            GroupsDto dto = new GroupsDto();
            dto.setCode(data.getCode());
            dto.setName(data.getName());
            if (data.getAccessCode() != null) {
                dto.setAccess_code(data.getAccessCode().getAccessNumber());
                dto.setAccessBudget(data.getAccessCode().getAccessBudget());
                dto.setAccessItem(data.getAccessCode().getAccessItem());
                dto.setAccessSupplier(data.getAccessCode().getAccessSupplier());
                dto.setAccessRequisition(data.getAccessCode().getAccessRequisition());
                dto.setAccessRfq(data.getAccessCode().getAccessRfq());
                dto.setAccessTrfRqst(data.getAccessCode().getAccessTrfRqst());
                dto.setAccessInvtrCsmb(data.getAccessCode().getAccessInvtrCsmb());
            }
            dtos.add(dto);
        });
        return dtos;
    }

    @Override
    public GroupsDto getGroupsById() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().isEmpty()) {
            return null;
        }
        String role = convertRole(auth.getAuthorities().iterator().next().getAuthority());

        if (role == null) {
            return null;
        }
        return companyGroupRepo.findById(role)
                .map(data -> {
                    GroupsDto dto = new GroupsDto();
                    dto.setCode(data.getCode());
                    dto.setName(data.getName());
                    if (data.getAccessCode() != null) {
                        dto.setAccess_code(data.getAccessCode().getAccessNumber());
                        dto.setAccessBudget(data.getAccessCode().getAccessBudget());
                        dto.setAccessItem(data.getAccessCode().getAccessItem());
                        dto.setAccessSupplier(data.getAccessCode().getAccessSupplier());
                        dto.setAccessRequisition(data.getAccessCode().getAccessRequisition());
                        dto.setAccessRfq(data.getAccessCode().getAccessRfq());
                        dto.setAccessTrfRqst(data.getAccessCode().getAccessTrfRqst());
                        dto.setAccessInvtrCsmb(data.getAccessCode().getAccessInvtrCsmb());
                    }
                    return dto;
                })
                .orElse(null);
    }

    public static String convertRole(String role) {
        return "com." + role.replaceFirst("^ROLE_", "")
                .toLowerCase()
                .replace("_", ".");
    }
}
