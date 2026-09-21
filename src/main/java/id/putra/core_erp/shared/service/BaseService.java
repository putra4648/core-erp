package id.putra.core_erp.shared.service;

public interface BaseService<T> {
    public T save(T entity);

    public T findById(Long id);

    public void delete(Long id);

    public void update(T entity);

    public Iterable<T> findAll();
}
