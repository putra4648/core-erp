package id.putra.core_erp.repository;

import java.util.List;

import id.putra.core_erp.entity.BaseEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.persistence.Table;

/**
 * Panache repository contract that turns hard deletes into soft deletes.
 *
 * <p>Every repository whose entity extends {@link BaseEntity} should implement this
 * instead of {@link PanacheRepository} directly. The only difference callers see is
 * {@link #softDeleteById(Long)} replacing {@code deleteById(Long)}.
 *
 * <p>Two responsibilities:
 * <ol>
 *   <li>{@link #softDeleteById} / {@link #softDelete} flip the {@code deleted} flag
 *       rather than issuing SQL {@code DELETE}.</li>
 *   <li>{@link #cascadeToChildren} gives subclasses a hook to soft-delete their own
 *       children. Default is a no-op, so leaf repositories need no override.</li>
 * </ol>
 *
 * <p>Reads need no changes at all: {@code @SQLRestriction("deleted = false")} on
 * {@link BaseEntity} already hides flagged rows from every JPA-generated query.
 *
 * @param <T> entity type, must extend {@link BaseEntity}
 */
public interface SoftDeleteRepository<T extends BaseEntity> extends PanacheRepository<T> {

	/**
	 * Soft-deletes the given id and everything below it. No-op when the id is absent
	 * or already soft-deleted ({@code findById} respects the SQL restriction).
	 *
	 * @param id the entity id
	 */
	default void softDeleteById(Long id) {
		findByIdOptional(id).ifPresent(this::softDelete);
	}

	/**
	 * Marks the entity deleted, then cascades to its children via
	 * {@link #cascadeToChildren}.
	 *
	 * @param entity the managed entity to flag
	 */
	default void softDelete(T entity) {
		entity.markDeleted(currentActor());
		cascadeToChildren(entity);
	}

	/**
	 * Hook for repositories whose entity owns child collections. Override to
	 * soft-delete children, e.g. {@code ZoneRepository} flags its {@code Rack} rows.
	 * Recursion happens naturally when the child repository's own {@code softDelete}
	 * is called. Default: do nothing.
	 *
	 * @param entity the parent entity that was just flagged
	 */
	default void cascadeToChildren(T entity) {
		// leaf entity, nothing to cascade
	}

	/**
	 * Un-deletes a row. Must go through native SQL: {@code @SQLRestriction} hides
	 * deleted rows from JPA queries, so the entity cannot be loaded normally.
	 *
	 * @param id the entity id
	 * @return number of rows updated (0 when the id does not exist)
	 */
	default int restore(Long id) {
		return getEntityManager()
				.createNativeQuery("update " + tableName()
						+ " set deleted = false, deleted_at = null, deleted_by = null where id = ?1")
				.setParameter(1, id)
				.executeUpdate();
	}

	/**
	 * Loads every row including soft-deleted ones. Native SQL for the same reason as
	 * {@link #restore(Long)}. Intended for admin/audit screens only.
	 *
	 * @return all rows, deleted and live
	 */
	@SuppressWarnings("unchecked")
	default List<T> findAllIncludingDeleted() {
		return getEntityManager()
				.createNativeQuery("select * from " + tableName(), getEntityClass())
				.getResultList();
	}

	/**
	 * Physical table name for this repository's entity, resolved from the JPA metamodel
	 * so it stays correct through {@code @Table} renames. Falls back to the simple class
	 * name when no {@code @Table} annotation is present, matching Hibernate's own default.
	 *
	 * @return the table name
	 */
	default String tableName() {
		Table table = getEntityClass().getAnnotation(Table.class);
		if (table != null && !table.name().isBlank()) {
			return table.name();
		}
		return getEntityClass().getSimpleName();
	}

	/**
	 * Entity class of this repository. Resolved from the generic type argument of the
	 * concrete implementing class — {@code class WarehouseRepository implements
	 * SoftDeleteRepository<Warehouse>} yields {@code Warehouse.class}.
	 *
	 * <p>Panache resolves its own type parameter the same way, so no extra metadata
	 * is needed. Repositories may override this to return a constant if they prefer.
	 *
	 * @return the entity class
	 */
	@SuppressWarnings("unchecked")
	default Class<T> getEntityClass() {
		Class<?> current = getClass();
		while (current != null && current != Object.class) {
			for (java.lang.reflect.Type iface : current.getGenericInterfaces()) {
				if (iface instanceof java.lang.reflect.ParameterizedType pt
						&& pt.getRawType() == SoftDeleteRepository.class) {
					return (Class<T>) pt.getActualTypeArguments()[0];
				}
			}
			current = current.getSuperclass();
		}
		throw new IllegalStateException(
				"Cannot resolve entity type for " + getClass().getName()
						+ "; implement SoftDeleteRepository with an explicit type argument"
						+ " or override getEntityClass()");
	}

	/**
	 * Best-effort actor identifier for audit columns. Returns {@code null} when no
	 * authenticated identity is available (background jobs, tests, bootstrap data).
	 *
	 * @return the current user's name, or {@code null}
	 */
	default String currentActor() {
		try {
			var handle = io.quarkus.arc.Arc.container()
					.instance(io.quarkus.security.identity.SecurityIdentity.class);
			if (handle == null || !handle.isAvailable()) {
				return null;
			}
			var identity = handle.get();
			return identity == null || identity.isAnonymous() ? null : identity.getPrincipal().getName();
		} catch (Exception e) {
			return null;
		}
	}
}
