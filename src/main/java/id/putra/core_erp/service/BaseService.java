package id.putra.core_erp.service;

import io.quarkus.panache.common.Page;

public interface BaseService<T> {
	public void save(T entity);

	public T findById(Long id);

	public void delete(Long id);

	public void update(T entity);

	public Iterable<T> findAll(T entity, Page page);
}
