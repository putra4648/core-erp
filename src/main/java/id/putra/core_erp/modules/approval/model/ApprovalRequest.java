package id.putra.core_erp.modules.approval.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "approval_requests")
public class ApprovalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "step_id", nullable = false)
    public ApprovalStep step;

    @Column(name = "reference_id", nullable = false)
    public String referenceId;

    @ManyToOne
    @JoinColumn(name = "workflow_id", nullable = false)
    public ApprovalWorkflow workflow;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public ApprovalStatus status;

    @Column(name = "submit_by", nullable = false)
    public String submittedBy;

    @CreationTimestamp
    @Column(name = "submit_at")
    public Instant submitAt;

    @OneToMany(mappedBy = "request", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    public List<ApprovalHistory> histories = new ArrayList<>();

}
