package id.putra.core_erp.modules.approval.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "approval_histories")
public class ApprovalHistory extends PanacheEntity {
    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    public ApprovalRequest request;

    @Column(name = "action", nullable = false)
    public String action;

    @Column(name = "comment", nullable = false)
    public String comment;

    @Column(name = "acted_by", nullable = false)
    public String actedBy;

    @CreationTimestamp
    @Column(name = "acted_at", nullable = false)
    public Instant actedAt;
}
