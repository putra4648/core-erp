package id.putra.core_erp.modules.procurement.service;

import java.util.List;

import id.putra.core_erp.modules.approval.enums.ApprovalEnum;
import id.putra.core_erp.modules.procurement.model.Requisition;

public interface RequisitionService {

    void add(Requisition entity);

    void update(Requisition entity);

    void delete(Requisition entity);

    Requisition findById(Long id);

    List<Requisition> findAll();

    Boolean checkApprovalFlow(Requisition dto);

    ApprovalEnum checkStatusApprovalIsPermanent(Requisition dto);

    byte[] download(Requisition dto) throws Exception;
}
