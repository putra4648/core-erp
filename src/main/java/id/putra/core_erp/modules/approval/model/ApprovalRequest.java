package id.putra.core_erp.modules.approval.model;

import java.time.Instant;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.enums.ApprovalType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "approval_requests")
public class ApprovalRequest extends PanacheEntity {

    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    public ApprovalType requestType;

    @Column(name = "current_stage", nullable = false)
    public Integer currentStage = 1;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public ApprovalStatus status = ApprovalStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt = Instant.now();

    @Column(name = "submit_by", nullable = false)
    public String submittedBy;

    @CreationTimestamp
    @Column(name = "submit_at")
    public Instant submitAt;

    @OneToMany(mappedBy = "request")
    public List<ApprovalHistory> histories;

}
