package id.putra.core_erp.warehouse.module.outbound.model.repository;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Shipment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShipmentRepository implements PanacheRepository<Shipment> {

}
