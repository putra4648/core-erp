package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import id.putra.core_erp.purchase_requisition.enums.ApprovalEnum;
import id.putra.core_erp.purchase_requisition.model.Requisition;
import io.vertx.mutiny.core.file.AsyncFile;

public interface RequisitionService {

    void add(Requisition entity);

    void update(Requisition entity);

    void delete(Requisition entity);

    Requisition findById(Long id);

    List<Requisition> findAll();

    Boolean checkApprovalFlow(Requisition dto);

    ApprovalEnum checkStatusApprovalIsPermanent(Requisition dto);

    AsyncFile download(Requisition dto);
}
