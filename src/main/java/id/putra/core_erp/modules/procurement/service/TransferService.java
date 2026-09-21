package id.putra.core_erp.modules.procurement.service;

import java.util.List;

import id.putra.core_erp.modules.approval.enums.ApprovalEnum;
import id.putra.core_erp.modules.procurement.model.Transfer;

public interface TransferService {

        void add(Transfer dto);

        void update(Transfer dto);

        void delete(Long id);

        List<Transfer> findAll(Transfer dto);

        Transfer getById(Transfer dto);

        Boolean checkApprovalFlow(Transfer dto);

        ApprovalEnum checkStatusApprovalIsPermanent(Transfer dto);

        byte[] download(Transfer dto) throws Exception;
}
