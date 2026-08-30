package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import id.putra.core_erp.purchase_requisition.dto.AccessDto;
import id.putra.core_erp.purchase_requisition.dto.GroupsDto;

public interface AccessService {
    List<AccessDto> getAccess();

    void updateAccess(List<GroupsDto> groupsDtoList) throws Exception;
}
