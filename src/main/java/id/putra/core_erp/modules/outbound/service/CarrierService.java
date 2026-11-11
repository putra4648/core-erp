package id.putra.core_erp.modules.outbound.service;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.Carrier;
import id.putra.core_erp.modules.outbound.repository.CarrierRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;

public class CarrierService implements BaseService<Carrier> {
	private final CarrierRepository carrierRepository;

	public CarrierService(CarrierRepository carrierRepository) {
		this.carrierRepository = carrierRepository;
	}

	@Override
	public void save(Carrier entity) {
		carrierRepository.persist(entity);
	}

	@Override
	public void update(Carrier dto) {
		carrierRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		carrierRepository.softDeleteById(id);
	}

	@Override
	public Carrier findById(Long id) {
		return carrierRepository.findById(id);
	}

	@Override
	public List<Carrier> findAll(Carrier c, Page p) {
		return carrierRepository.findAll().list();
	}
}
