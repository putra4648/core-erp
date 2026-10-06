package id.putra.core_erp.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity extends PanacheEntity {

<<<<<<< HEAD
	@Column
	@CreationTimestamp
	private Instant createAt;

	@Column
	private String createBy;

	@Column
	@UpdateTimestamp
	private Instant updateAt;

	@Column
=======
	@Column(name = "create_at")
	@CreationTimestamp
	private Instant createAt;

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "update_at")
	@UpdateTimestamp
	private Instant updateAt;

	@Column(name = "update_by")
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	private String updateBy;

	public Instant getCreateAt() {
		return createAt;
	}

<<<<<<< HEAD
	public String getCreateBy() {
		return createBy;
	}

	public Instant getUpdateAt() {
		return updateAt;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setCreateAt(Instant createdAt) {
		this.createAt = createdAt;
	}

	public void setCreateBy(String createdBy) {
		this.createBy = createdBy;
	}

	public void setUpdateAt(Instant updatedAt) {
		this.updateAt = updatedAt;
=======
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
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	public void setUpdateBy(String updatedBy) {
		this.updateBy = updatedBy;
	}
}
