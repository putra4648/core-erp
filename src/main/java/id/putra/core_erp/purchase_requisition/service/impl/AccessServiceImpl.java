package id.putra.core_erp.purchase_requisition.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.purchase_requisition.constant.MessageConstant;
import id.putra.core_erp.purchase_requisition.dto.AccessDto;
import id.putra.core_erp.purchase_requisition.dto.GroupsDto;
import id.putra.core_erp.purchase_requisition.model.Access;
import id.putra.core_erp.purchase_requisition.repository.AccessRepo;
import id.putra.core_erp.purchase_requisition.service.AccessService;
import lombok.RequiredArgsConstructor;

@Service("accessService")
@RequiredArgsConstructor
@Transactional
public class AccessServiceImpl implements AccessService {
    private final AccessRepo companyAccessRepo;

    @Override
    public List<AccessDto> getAccess() {
        List<AccessDto> dtos = new ArrayList<>();
        companyAccessRepo.findAll().forEach(data -> {
            AccessDto dto = new AccessDto();
            dto.setAccessNumber(data.getAccessNumber());
            dto.setAccessBudget(data.getAccessBudget());
            dto.setAccessItem(data.getAccessItem());
            dto.setAccessSupplier(data.getAccessSupplier());
            dto.setAccessRequisition(data.getAccessRequisition());
            dto.setAccessRfq(data.getAccessRfq());
            dto.setAccessTrfRqst(data.getAccessTrfRqst());
            dto.setAccessInvtrCsmb(data.getAccessInvtrCsmb());
            dtos.add(dto);
        });
        return dtos;
    }

    @Override
    public void updateAccess(List<GroupsDto> groupsDtoList) throws Exception {
        for (GroupsDto groupsDto : groupsDtoList) {
            if (groupsDto.getCode() != null) {
                Optional<Access> accessOptional = companyAccessRepo
                        .findById(Objects.requireNonNull(groupsDto.getAccess_code()));

                if (accessOptional.isPresent()) {
                    Access updatedAccess = dtoToEntity(accessOptional.get(), groupsDto);
                    normalizeAccessFields(updatedAccess);
                    companyAccessRepo.save(Objects.requireNonNull(updatedAccess));
                } else {
                    String message = MessageFormat.format(MessageConstant.NOT_FOUND, groupsDto.getCode());
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    private Access dtoToEntity(Access entity, GroupsDto body) {
        entity.setAccessItem(body.getAccessItem());
        entity.setAccessSupplier(body.getAccessSupplier());
        entity.setAccessBudget(body.getAccessBudget());
        entity.setAccessRequisition(body.getAccessRequisition());
        entity.setAccessRfq(body.getAccessRfq());
        entity.setAccessTrfRqst(body.getAccessTrfRqst());
        entity.setAccessInvtrCsmb(body.getAccessInvtrCsmb());
        return entity;
    }

    private void normalizeAccessFields(Access access) {
        if (access.getAccessSupplier() == null)
            access.setAccessSupplier("INACTIVE");
        if (access.getAccessItem() == null)
            access.setAccessItem("INACTIVE");
        if (access.getAccessBudget() == null)
            access.setAccessBudget("INACTIVE");
        if (access.getAccessRequisition() == null)
            access.setAccessRequisition("INACTIVE");
        if (access.getAccessRfq() == null)
            access.setAccessRfq("INACTIVE");
        if (access.getAccessTrfRqst() == null)
            access.setAccessTrfRqst("INACTIVE");
        if (access.getAccessInvtrCsmb() == null)
            access.setAccessInvtrCsmb("INACTIVE");
    }
}
