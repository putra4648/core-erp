package id.putra.core_erp.service;

import java.util.List;

import io.quarkus.panache.common.Page;

public interface BaseService<T> {
	public void delete(Long id);

	public List<T> findAll(T entity, Page page);

	public T findById(Long id);

	public void save(T entity);

	public void update(T entity);
}
