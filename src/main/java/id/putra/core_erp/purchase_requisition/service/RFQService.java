package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import id.putra.core_erp.purchase_requisition.enums.ApprovalEnum;
import id.putra.core_erp.purchase_requisition.model.RFQ;
import io.vertx.mutiny.core.file.AsyncFile;

public interface RFQService {
    void add(RFQ entity);

    void update(RFQ entity);

    RFQ getById(Long id);

    List<RFQ> findAll();

    void delete(Long id);

    Boolean checkApprovalFlow(RFQ dto);

    ApprovalEnum checkStatusApprovalIsPermanent(RFQ dto);

    AsyncFile download(RFQ dto);
}
