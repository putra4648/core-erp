package id.putra.core_erp.modules.warehouse.repository;

import id.putra.core_erp.modules.warehouse.model.Rack;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RackRepository implements SoftDeleteRepository<Rack> {

}
