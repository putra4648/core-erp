package id.putra.core_erp.modules.approval.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import id.putra.core_erp.enums.ApprovalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "approval_histories")
public class ApprovalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public Long id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    public ApprovalRequest request;

    @ManyToOne
    @JoinColumn(name = "step_id", nullable = false)
    public ApprovalStep step;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    public ApprovalStatus action;

    @Column(name = "comment", nullable = false)
    public String comment;

    @Column(name = "acted_by", nullable = false)
    public String actedBy;

    @CreationTimestamp
    @Column(name = "acted_at", nullable = false)
    public Instant actedAt;
}
