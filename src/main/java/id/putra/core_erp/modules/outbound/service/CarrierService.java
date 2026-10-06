package id.putra.core_erp.modules.outbound.service;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
import id.putra.core_erp.modules.outbound.model.Carrier;
import id.putra.core_erp.modules.outbound.repository.CarrierRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
<<<<<<< HEAD
=======
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

public class CarrierService implements BaseService<Carrier> {
	private final CarrierRepository carrierRepository;

<<<<<<< HEAD
	public CarrierService(CarrierRepository carrierRepository) {
		this.carrierRepository = carrierRepository;
	}

	@Override
	public void delete(Long id) {
		carrierRepository.deleteById(id);
	}

	@Override
	public List<Carrier> findAll(Carrier s, Page p) {
		return carrierRepository.findAll().list();
	}

	@Override
	public Carrier findById(Long id) {
		return carrierRepository.findById(id);
	}

	@Override
	public void save(Carrier entity) {
		carrierRepository.persist(entity);
	}

	@Override
	public void update(Carrier entity) {
		carrierRepository.getEntityManager().merge(entity);
	}
}
=======
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
		carrierRepository.deleteById(id);
	}

	@Override
	public Carrier findById(Long id) {
		return carrierRepository.findById(id);
	}

	@Override
	public Iterable<Carrier> findAll(Carrier c, Page p) {
		return carrierRepository.findAll().list();
	}
}
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
