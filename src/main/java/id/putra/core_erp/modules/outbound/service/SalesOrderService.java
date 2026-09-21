package id.putra.core_erp.modules.outbound.service;

import id.putra.core_erp.shared.service.BaseService;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.modules.outbound.model.SalesOrder;
import id.putra.core_erp.modules.outbound.repository.SalesOrderRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class SalesOrderService implements BaseService<SalesOrder> {

    private SalesOrderRepository salesOrderRepository;
    public void create(SalesOrder dto) {
        salesOrderRepository.persist(dto);
    }
    public SalesOrder save(SalesOrder dto) {
        create(dto);
        return dto;
    }
    public SalesOrder findById(Long id) {
        return salesOrderRepository.findById(id);
    }
    public Iterable<SalesOrder> findAll() {
        return salesOrderRepository.findAll().list();
    }
    public void update(SalesOrder dto) {
        salesOrderRepository.getEntityManager().merge(dto);
    }
    public void delete(Long id) {
        salesOrderRepository.deleteById(id);
    }

    public SalesOrder getById(Long id) {
        return salesOrderRepository.findById(id);
    }
    public DataProvider<SalesOrder, Void> getAll(String value, Page page) {
        String pattern = "%" + (value == null ? "" : value.trim().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> salesOrderRepository.find("lower(soNumber) like ?1", pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(salesOrderRepository.count("lower(soNumber) like ?1", pattern)));
    }

}
