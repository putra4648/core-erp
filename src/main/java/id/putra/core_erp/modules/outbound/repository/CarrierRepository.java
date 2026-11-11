package id.putra.core_erp.modules.outbound.repository;

import id.putra.core_erp.modules.outbound.model.Carrier;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CarrierRepository implements SoftDeleteRepository<Carrier> {

}
