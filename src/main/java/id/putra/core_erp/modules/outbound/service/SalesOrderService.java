package id.putra.core_erp.modules.outbound.service;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
import id.putra.core_erp.modules.outbound.model.SalesOrder;
import id.putra.core_erp.modules.outbound.repository.SalesOrderRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class SalesOrderService implements BaseService<SalesOrder> {
	private SalesOrderRepository salesOrderRepository;

<<<<<<< HEAD
	@Override
	public void delete(Long id) {
		salesOrderRepository.deleteById(id);
	}

	@Override
	public List<SalesOrder> findAll(SalesOrder so, Page p) {
		return salesOrderRepository.findAll().list();
=======
	private SalesOrderRepository salesOrderRepository;

	@Override
	public void save(SalesOrder dto) {
		salesOrderRepository.persist(dto);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	@Override
	public SalesOrder findById(Long id) {
		return salesOrderRepository.findById(id);
	}

	@Override
<<<<<<< HEAD
	public void save(SalesOrder dto) {
		salesOrderRepository.persist(dto);
=======
	public Iterable<SalesOrder> findAll(SalesOrder so, Page p) {
		return salesOrderRepository.findAll().list();
	}

	@Override
	public void update(SalesOrder dto) {
		salesOrderRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		salesOrderRepository.deleteById(id);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

	@Override
	public void update(SalesOrder dto) {
		salesOrderRepository.getEntityManager().merge(dto);
	}
}
