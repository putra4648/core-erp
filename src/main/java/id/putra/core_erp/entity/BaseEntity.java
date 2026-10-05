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
	private Instant createAt;

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "update_at")
	@UpdateTimestamp
	private Instant updateAt;

	@Column(name = "update_by")
	private String updateBy;

	public Instant getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Instant createdAt) {
		this.createAt = createdAt;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createdBy) {
		this.createBy = createdBy;
	}

	public Instant getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Instant updatedAt) {
		this.updateAt = updatedAt;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updatedBy) {
		this.updateBy = updatedBy;
	}
}
