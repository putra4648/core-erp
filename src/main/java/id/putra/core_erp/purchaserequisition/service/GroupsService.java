package id.putra.core_erp.purchaserequisition.service;

import java.util.List;
import id.putra.core_erp.purchaserequisition.dto.GroupsDto;

public interface GroupsService {
    List<GroupsDto> getGroups();

    GroupsDto getGroupsById();
}

