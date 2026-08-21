package id.putra.core_erp.purchaserequisition.service;

import id.putra.core_erp.purchaserequisition.dto.RequisitionDto;
import id.putra.core_erp.purchaserequisition.enums.ApprovalEnum;

public interface RequisitionService extends AppData<RequisitionDto>, AppApproval<RequisitionDto, ApprovalEnum>,
                AppDownloadFile<RequisitionDto>, AppPaginationData<RequisitionDto> {

}

