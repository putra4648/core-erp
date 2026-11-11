package id.putra.core_erp.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * Shared base for every audited entity in the ERP.
 *
 * <p>Soft delete contract: {@code @SQLRestriction("deleted = false")} is applied at the
 * mapped-superclass level, so Hibernate injects the predicate into every generated
 * SELECT/UPDATE/DELETE against a subclass — including {@code findById}, {@code findAll},
 * {@code count} and association joins. Rows are therefore invisible the moment
 * {@link #markDeleted(String)} runs, with no per-query filtering required.
 *
 * <p>The restriction does <b>not</b> cover native SQL. Repositories issuing native queries
 * must add {@code deleted = false} to their WHERE clause by hand.
 *
 * <p>The restriction also does not block a hard delete: {@code PanacheEntity.deleteById()}
 * still emits a real {@code DELETE}. Callers go through
 * {@code SoftDeleteRepository#softDeleteById} instead, which flips the flag.
 */
@MappedSuperclass
@SQLRestriction("deleted = false")
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

	/**
	 * Soft delete flag. {@code columnDefinition} carries an explicit DEFAULT so
	 * {@code quarkus.hibernate-orm.database.generation=update} can add the column to
	 * already-populated tables (PostgreSQL rejects NOT NULL ADD COLUMN without a default).
	 */
	@Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
	public boolean deleted = false;

	@Column(name = "deleted_at")
	public Instant deletedAt;

	@Column(name = "deleted_by")
	public String deletedBy;

	/**
	 * Flags this row as deleted. Does not cascade and does not persist — the caller's
	 * transaction flushes the change. Cascading is the repository's job, see
	 * {@code SoftDeleteRepository#cascadeToChildren}.
	 *
	 * @param actor identifier of the user performing the delete, may be {@code null}
	 */
	public void markDeleted(String actor) {
		this.deleted = true;
		this.deletedAt = Instant.now();
		this.deletedBy = actor;
	}

	/** Clears the soft delete flags. Reverses {@link #markDeleted(String)}. */
	public void clearDeleted() {
		this.deleted = false;
		this.deletedAt = null;
		this.deletedBy = null;
	}

}
