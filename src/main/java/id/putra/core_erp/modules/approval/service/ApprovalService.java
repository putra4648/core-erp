package id.putra.core_erp.modules.approval.service;

import java.util.UUID;

import id.putra.core_erp.modules.approval.model.ApprovalEvent;
import id.putra.core_erp.modules.approval.model.ApprovalRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ApprovalService {

    private final Event<ApprovalEvent> statusEventPublisher;

    public ApprovalService(Event<ApprovalEvent> statusEventPublisher) {
        this.statusEventPublisher = statusEventPublisher;
    }

    @Transactional
    public void executeAction(UUID requestId, UUID actorId, String action, String comment) {
        ApprovalRequest request = ApprovalRequest.findById(requestId);

        if (request == null) {
            throw new NotFoundException("Approval request not found");
        }

        if ("REJECT".equalsIgnoreCase(action)) {
            request.status = ApprovalRequest.Status.REJECTED;

            // Publish Event bahwa transaksi ditolak
            statusEventPublisher.fire(new ApprovalEvent(
                    request.entityId, request.entityType, request.status, request.currentStage));
            return;
        }

        // Cek stage berikutnya
        boolean hasNextStage = checkNextStageExists(request.entityType, request.currentStage);

        if (hasNextStage) {
            request.currentStage += 1;
        } else {
            request.status = ApprovalRequest.Status.APPROVED;

            // Publish Event bahwa transaksi disetujui penuh
            statusEventPublisher.fire(new ApprovalEvent(
                    request.entityId, request.entityType, request.status, request.currentStage));
        }
    }

    private boolean checkNextStageExists(String entityType, int currentStage) {
        // Logika cek stage lanjutan dari database/config
        return currentStage < 2;
    }
}
