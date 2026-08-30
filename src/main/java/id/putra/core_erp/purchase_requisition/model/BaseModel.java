package id.putra.core_erp.purchase_requisition.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseModel {

    @Column(name = "created_by")
    private String userCreateBy;

    @Column(name = "created_date")
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdDate;

    @Column(name = "updated_by")
    private String userUpdateBy;

    @Column(name = "updated_date")
    @UpdateTimestamp(source = SourceType.DB)
    private Instant updatedDate;

    /// Before any insert data
    @PrePersist
    public void beforeAnyInsert() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        this.setUserCreateBy(name);
    }

    @PreUpdate
    public void beforeAnyUpdate() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        this.setUserUpdateBy(name);
    }
}
