package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.outbound.model.entity.SalesOrder;
import id.putra.core_erp.warehouse.module.outbound.model.repository.SalesOrderRepository;
import id.putra.core_erp.warehouse.module.outbound.service.core.SalesOrderService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {
    @Inject
    private SalesOrderRepository salesOrderRepository;

    @Override
    public void create(SalesOrder dto) {
        salesOrderRepository.persist(dto);
    }

    @Override
    public void update(SalesOrder dto) {
        salesOrderRepository.getEntityManager().merge(dto);
    }

    @Override
    public void delete(Long id) {
        salesOrderRepository.deleteById(id);
    }

    @Override
    public SalesOrder getById(Long id) {
        return salesOrderRepository.findById(id);
    }

    @Override
    public DataProvider<SalesOrder, Void> getAll(String value, Page page) {
        String pattern = "%" + (value == null ? "" : value.trim().toLowerCase()) + "%";
        return DataProvider.fromCallbacks(
                query -> salesOrderRepository.find("lower(soNumber) like ?1", pattern)
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(salesOrderRepository.count("lower(soNumber) like ?1", pattern)));
    }

}
