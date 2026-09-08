package id.putra.core_erp.purchase_requisition.service;

import id.putra.core_erp.purchase_requisition.dto.AppDto;
import io.vertx.mutiny.core.file.AsyncFile;

public interface AppDownloadFile<S> {
    AsyncFile download(AppDto<S> dto);
}
