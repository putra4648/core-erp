package id.putra.core_erp.modules.warehouse.repository;

import id.putra.core_erp.modules.warehouse.model.Warehouse;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class WarehouseRepository implements SoftDeleteRepository<Warehouse> {

	@Inject
	ZoneRepository zoneRepository;

	/**
	 * Warehouse is the top of the storage hierarchy, so this cascade runs two levels
	 * deep: Warehouse → Zone → Rack. ZoneRepository#cascadeToChildren handles the
	 * second hop; recursion stops there because Rack is a leaf.
	 */
	@Override
	public void cascadeToChildren(Warehouse warehouse) {
		zoneRepository.find("warehouse.id", warehouse.id).list().forEach(zoneRepository::softDelete);
	}

}
