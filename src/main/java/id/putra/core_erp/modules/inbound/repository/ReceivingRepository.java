package id.putra.core_erp.modules.inbound.repository;

import id.putra.core_erp.modules.inbound.model.Receiving;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReceivingRepository implements SoftDeleteRepository<Receiving> {

	@Inject
	ReceivingLineRepository receivingLineRepository;

	/** Lines are part of the Receiving aggregate — flag them with the header. */
	@Override
	public void cascadeToChildren(Receiving receiving) {
		receivingLineRepository.find("receiving.id", receiving.id).list()
				.forEach(receivingLineRepository::softDelete);
	}
}
