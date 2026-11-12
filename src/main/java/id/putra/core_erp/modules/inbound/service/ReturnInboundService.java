package id.putra.core_erp.modules.inbound.service;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.Return;
import id.putra.core_erp.modules.inbound.repository.ReturnRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ReturnInboundService implements BaseService<Return> {
	private final ReturnRepository returnRepository;

	public ReturnInboundService(ReturnRepository returnRepository) {
		this.returnRepository = returnRepository;
	}

	@Override
	public void save(Return entity) {
		returnRepository.persist(entity);
	}

	@Override
	public void update(Return dto) {
		returnRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		returnRepository.softDeleteById(id);
	}

	@Override
	public Return findById(Long id) {
		return returnRepository.findById(id);
	}

	public Return getById(Long id) {
		return findById(id);
	}

	@Override
	public List<Return> findAll(Return r, Page p) {
		return returnRepository.findAll().list();
	}

	@Override
	public long count() {
		return returnRepository.count();
	}
}
