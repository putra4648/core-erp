package id.putra.core_erp.purchaserequisition.service;

import java.util.List;
import id.putra.core_erp.purchaserequisition.dto.AccessDto;
import id.putra.core_erp.purchaserequisition.dto.GroupsDto;

public interface AccessService {
    List<AccessDto> getAccess();
    void updateAccess(List<GroupsDto> groupsDtoList) throws Exception;
}

