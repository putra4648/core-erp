package id.putra.core_erp.modules.approval.model;

import java.util.UUID;

public record ApprovalEvent(
        UUID entityId,
        String entityType,
        ApprovalRequest.Status status,
        Integer stage) {
}