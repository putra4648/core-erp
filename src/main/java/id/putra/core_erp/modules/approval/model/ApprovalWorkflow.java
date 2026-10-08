package id.putra.core_erp.modules.approval.model;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "approval_workflows")
public class ApprovalWorkflow extends PanacheEntity {

    @Column(name = "workflow_name", nullable = false)
    @NotBlank(message = "Workflow name is required")
    public String workflowName;

    @Column(name = "request_type", nullable = false)
    @NotBlank(message = "Request type is required")
    public String requestType;

    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public List<ApprovalStep> steps;
}
