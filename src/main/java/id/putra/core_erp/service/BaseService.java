package id.putra.core_erp.service;

import java.util.List;

import io.quarkus.panache.common.Page;

public interface BaseService<T> {
<<<<<<< HEAD
	public void delete(Long id);

	public List<T> findAll(T entity, Page page);

	public T findById(Long id);

	public void save(T entity);

	public void update(T entity);
=======
	public void save(T entity);

	public T findById(Long id);

	public void delete(Long id);

	public void update(T entity);

	public Iterable<T> findAll(T entity, Page page);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
}
