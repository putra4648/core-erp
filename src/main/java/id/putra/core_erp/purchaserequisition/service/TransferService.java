package id.putra.core_erp.purchaserequisition.service;

import id.putra.core_erp.purchaserequisition.dto.TransferDto;
import id.putra.core_erp.purchaserequisition.enums.ApprovalEnum;

public interface TransferService
                extends AppData<TransferDto>, AppApproval<TransferDto, ApprovalEnum>,
                AppDownloadFile<TransferDto> {

}

