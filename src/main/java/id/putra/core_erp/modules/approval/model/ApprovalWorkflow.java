package id.putra.core_erp.modules.approval.model;

import java.util.ArrayList;
import java.util.List;

import id.putra.core_erp.enums.ApprovalType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "approval_workflows")
public class ApprovalWorkflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "workflow_name", nullable = false)
    @NotBlank(message = "Workflow name is required")
    public String workflowName;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false, unique = true)
    @NotNull(message = "Request type is required")
    public ApprovalType requestType;

    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepOrder ASC")
    public List<ApprovalStep> steps = new ArrayList<>();
}
