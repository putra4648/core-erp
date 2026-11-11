package id.putra.core_erp.modules.outbound.service;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.SalesOrder;
import id.putra.core_erp.modules.outbound.repository.SalesOrderRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class SalesOrderService implements BaseService<SalesOrder> {
	private final SalesOrderRepository salesOrderRepository;

	public SalesOrderService(SalesOrderRepository salesOrderRepository) {
		this.salesOrderRepository = salesOrderRepository;
	}

	@Override
	public void save(SalesOrder dto) {
		salesOrderRepository.persist(dto);
	}

	@Override
	public SalesOrder findById(Long id) {
		return salesOrderRepository.findById(id);
	}

	@Override
	public List<SalesOrder> findAll(SalesOrder so, Page p) {
		return salesOrderRepository.findAll().list();
	}

	@Override
	public void update(SalesOrder dto) {
		salesOrderRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		salesOrderRepository.softDeleteById(id);
	}

}
