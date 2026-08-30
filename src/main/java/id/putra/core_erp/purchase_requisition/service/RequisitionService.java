package id.putra.core_erp.purchase_requisition.service;

import id.putra.core_erp.purchase_requisition.dto.RequisitionDto;
import id.putra.core_erp.purchase_requisition.enums.ApprovalEnum;

public interface RequisitionService extends AppData<RequisitionDto>, AppApproval<RequisitionDto, ApprovalEnum>,
        AppDownloadFile<RequisitionDto>, AppPaginationData<RequisitionDto> {

}
