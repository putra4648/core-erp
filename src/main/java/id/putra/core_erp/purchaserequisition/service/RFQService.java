package id.putra.core_erp.purchaserequisition.service;

import id.putra.core_erp.purchaserequisition.dto.RFQDto;
import id.putra.core_erp.purchaserequisition.enums.ApprovalEnum;

public interface RFQService extends AppData<RFQDto>, AppApproval<RFQDto, ApprovalEnum>, AppDownloadFile<RFQDto> {

}

