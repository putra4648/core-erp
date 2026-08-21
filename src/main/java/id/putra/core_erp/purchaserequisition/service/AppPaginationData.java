package id.putra.core_erp.purchaserequisition.service;

import java.util.List;
import id.putra.core_erp.purchaserequisition.dto.AppDto;

public interface AppPaginationData<S> {
    List<S> getPaginationData(AppDto<S> params);
}

