package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import id.putra.core_erp.purchase_requisition.dto.AppDto;

public interface AppPaginationData<S> {
    List<S> getPaginationData(AppDto<S> params);
}
