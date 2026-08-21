package id.putra.core_erp.purchaserequisition.service;

import org.springframework.core.io.Resource;
import id.putra.core_erp.purchaserequisition.dto.AppDto;

public interface AppDownloadFile<S> {
    Resource download(AppDto<S> dto);
}

