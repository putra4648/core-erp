package id.putra.core_erp.warehouse.module.user.model.entity;

import java.time.ZonedDateTime;

import id.putra.core_erp.shared.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class AuditLog extends BaseEntity {
    @Id
    @Column(name = "audit_id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    private String entity;
    private String action;
    private ZonedDateTime timestamp;
    private String ipAddress;

    @Lob
    private String oldValue;
    @Lob
    private String newValue;
}
