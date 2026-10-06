package id.putra.core_erp.modules.inbound.service;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.Receiving;
import id.putra.core_erp.modules.inbound.repository.ReceivingRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ReceivingOrderService implements BaseService<Receiving> {

	private final ReceivingRepository receivingRepository;

	public ReceivingOrderService(ReceivingRepository receivingRepository) {
		this.receivingRepository = receivingRepository;
	}

	@Override
	public void save(Receiving entity) {
		receivingRepository.persist(entity);
	}

	@Override
	public Receiving findById(Long id) {
		return receivingRepository.findById(id);
	}

	@Override
	public void delete(Long id) {
		receivingRepository.deleteById(id);
	}

	@Override
	public void update(Receiving entity) {
		receivingRepository.getEntityManager().merge(entity);
	}

	@Override
	public List<Receiving> findAll(Receiving entity, Page page) {
		return receivingRepository.findAll().page(page).list();
	}

}
