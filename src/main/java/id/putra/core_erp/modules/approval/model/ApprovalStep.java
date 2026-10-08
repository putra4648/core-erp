package id.putra.core_erp.modules.approval.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "approval_steps")
public class ApprovalStep extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "workflow_id", nullable = false)
    public ApprovalWorkflow workflow;

    @NotBlank(message = "Role required is required")
    @Column(name = "role_required", nullable = false)
    public String roleRequired;

    @NotNull(message = "Step order is required")
    @Column(name = "step_order", nullable = false)
    public Integer stepOrder;

    @Column(name = "is_optional", nullable = false)
    public Boolean optional = false;

}
