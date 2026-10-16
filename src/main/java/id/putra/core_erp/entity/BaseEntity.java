package id.putra.core_erp.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity extends PanacheEntity {

	@Column(name = "create_at")
	@CreationTimestamp
	public Instant createAt;

	@Column(name = "create_by")
	public String createBy;

	@Column(name = "update_at")
	@UpdateTimestamp
	public Instant updateAt;

	@Column(name = "update_by")
	public String updateBy;

}
