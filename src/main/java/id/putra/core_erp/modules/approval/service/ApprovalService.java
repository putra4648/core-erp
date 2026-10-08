package id.putra.core_erp.modules.approval.service;

import id.putra.core_erp.modules.approval.model.ApprovalHistory;
import id.putra.core_erp.modules.approval.model.ApprovalRequest;
import io.quarkus.vertx.ConsumeEvent;
import id.putra.core_erp.modules.approval.repository.ApprovalHistoryRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ApprovalService {
    private final ApprovalHistoryRepo approvalHistoryRepo;

    public ApprovalService(ApprovalHistoryRepo approvalHistoryRepo) {
        this.approvalHistoryRepo = approvalHistoryRepo;
    }

    @ConsumeEvent
    public void approvalLogEvent(ApprovalHistory history) {
        approvalHistoryRepo.persist(history);
    }

    @Transactional
    public void submit(ApprovalRequest req) {

    }

    @Transactional
    public void approve(ApprovalRequest req) {

    }

    @Transactional
    public void reject(ApprovalRequest req) {

    }
}
