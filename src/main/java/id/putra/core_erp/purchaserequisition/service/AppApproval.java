package id.putra.core_erp.purchaserequisition.service;

import id.putra.core_erp.purchaserequisition.dto.AppDto;

public interface AppApproval<S, T extends Enum<T>> {
    /* Check current approval status */
    Boolean checkApprovalFlow(AppDto<S> dto);

    /* Check if current approval is already approved or not */
    T checkStatusApprovalIsPermanent(AppDto<S> dto);
}

