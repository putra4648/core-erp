package id.putra.core_erp.modules.procurement.service;

import java.util.List;

import id.putra.core_erp.modules.approval.enums.ApprovalEnum;
import id.putra.core_erp.modules.procurement.model.Quotation;

public interface QuotationService {
    void add(Quotation entity);

    void update(Quotation entity);

    Quotation getById(Long id);

    List<Quotation> findAll(Quotation params);

    void delete(Long id);

    Boolean checkApprovalFlow(Quotation dto);

    ApprovalEnum checkStatusApprovalIsPermanent(Quotation dto);

    byte[] download(Quotation dto) throws Exception;
}
