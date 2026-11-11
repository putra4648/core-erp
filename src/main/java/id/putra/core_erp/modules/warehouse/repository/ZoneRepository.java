package id.putra.core_erp.modules.warehouse.repository;

import id.putra.core_erp.modules.warehouse.model.Zone;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ZoneRepository implements SoftDeleteRepository<Zone> {

	@Inject
	RackRepository rackRepository;

	/** Soft-deleting a zone takes its racks with it — Zone owns Rack as an aggregate root. */
	@Override
	public void cascadeToChildren(Zone zone) {
		rackRepository.find("zone.id", zone.id).list().forEach(rackRepository::softDelete);
	}
}
