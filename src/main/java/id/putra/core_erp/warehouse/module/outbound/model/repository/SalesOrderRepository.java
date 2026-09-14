package id.putra.core_erp.warehouse.module.outbound.model.repository;

import id.putra.core_erp.warehouse.module.outbound.model.entity.SalesOrder;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SalesOrderRepository implements PanacheRepositoryBase<SalesOrder, Long> {

}
