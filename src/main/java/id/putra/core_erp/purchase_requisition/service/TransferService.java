package id.putra.core_erp.purchase_requisition.service;

import id.putra.core_erp.purchase_requisition.dto.TransferDto;
import id.putra.core_erp.purchase_requisition.enums.ApprovalEnum;

public interface TransferService
        extends AppData<TransferDto>, AppApproval<TransferDto, ApprovalEnum>,
        AppDownloadFile<TransferDto> {

}
