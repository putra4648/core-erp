package id.putra.core_erp.modules.outbound.service;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.Shipment;
import id.putra.core_erp.modules.outbound.repository.ShipmentRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ShippingService implements BaseService<Shipment> {
	private final ShipmentRepository shipmentRepository;

	public ShippingService(ShipmentRepository shipmentRepository) {
		this.shipmentRepository = shipmentRepository;
	}

	@Override
	public void save(Shipment dto) {
		shipmentRepository.persist(dto);
	}

	@Override
	public void update(Shipment dto) {
		shipmentRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		shipmentRepository.deleteById(id);
	}

	@Override
	public Shipment findById(Long id) {
		return shipmentRepository.findById(id);
	}

	@Override
	public List<Shipment> findAll(Shipment s, Page p) {
		return shipmentRepository.findAll().list();
	}

}
