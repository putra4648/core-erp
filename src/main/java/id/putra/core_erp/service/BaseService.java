package id.putra.core_erp.service;

import java.util.List;

import io.quarkus.panache.common.Page;

public interface BaseService<T> {
	public void save(T entity);

	public T findById(Long id);

	public void delete(Long id);

	public void update(T entity);

	public List<T> findAll(T entity, Page page);

	/**
	 * Number of live rows for this service's entity. Soft-deleted rows are excluded
	 * automatically by the {@code @SQLRestriction} on {@code BaseEntity}.
	 *
	 * <p>Exists so a view can show a count without injecting the repository — the
	 * service stays the only thing other modules and views reach for.
	 */
	public long count();
}
