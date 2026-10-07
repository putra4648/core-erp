package id.putra.core_erp.modules.approval.model;

import java.time.Instant;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "approval_requests")
public class ApprovalRequest extends PanacheEntity {

    @Column(name = "entity_type", nullable = false)
    public String entityType; // Contoh: 'LEAVE_REQUEST', 'PURCHASE_ORDER'

    @Column(name = "entity_id", nullable = false)
    public UUID entityId; // ID transaksi di modul asal

    @Column(name = "current_stage", nullable = false)
    public Integer currentStage = 1;

    @Enumerated(EnumType.STRING)
    public Status status = Status.PENDING;

    @Column(name = "created_at")
    public Instant createdAt = Instant.now();

    public enum Status {
        PENDING, APPROVED, REJECTED, REVISED
    }
}
