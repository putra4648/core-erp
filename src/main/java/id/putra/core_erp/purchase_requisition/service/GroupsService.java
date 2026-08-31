package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import id.putra.core_erp.purchase_requisition.dto.GroupsDto;

public interface GroupsService {
    List<GroupsDto> getGroups();

    GroupsDto getGroupsById();
}
