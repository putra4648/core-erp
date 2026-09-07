package id.putra.core_erp.purchase_requisition.service;

import id.putra.core_erp.purchase_requisition.dto.AppDto;
import jakarta.annotation.Resource;

public interface AppDownloadFile<S> {
    Resource download(AppDto<S> dto);
}
