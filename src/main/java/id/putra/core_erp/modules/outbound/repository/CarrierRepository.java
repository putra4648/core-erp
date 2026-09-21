package id.putra.core_erp.modules.outbound.repository;

import id.putra.core_erp.modules.outbound.model.Carrier;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CarrierRepository implements PanacheRepository<Carrier> {

}
