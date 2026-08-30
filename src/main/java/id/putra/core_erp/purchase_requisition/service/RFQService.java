package id.putra.core_erp.purchase_requisition.service;

import id.putra.core_erp.purchase_requisition.dto.RFQDto;
import id.putra.core_erp.purchase_requisition.enums.ApprovalEnum;

public interface RFQService extends AppData<RFQDto>, AppApproval<RFQDto, ApprovalEnum>, AppDownloadFile<RFQDto> {

}
