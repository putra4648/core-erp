package id.putra.core_erp.purchase_requisition.service;

import org.springframework.core.io.Resource;

import id.putra.core_erp.purchase_requisition.dto.AppDto;

public interface AppDownloadFile<S> {
    Resource download(AppDto<S> dto);
}
