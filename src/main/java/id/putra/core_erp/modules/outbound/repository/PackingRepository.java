package id.putra.core_erp.modules.outbound.repository;

import id.putra.core_erp.modules.outbound.model.Packing;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PackingRepository implements PanacheRepositoryBase<Packing, Long> {

}
