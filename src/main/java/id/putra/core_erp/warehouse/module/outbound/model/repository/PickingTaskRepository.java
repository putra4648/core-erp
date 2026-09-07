package id.putra.core_erp.warehouse.module.outbound.model.repository;

import id.putra.core_erp.warehouse.module.outbound.model.entity.PickingTask;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PickingTaskRepository implements PanacheRepositoryBase<PickingTask, Long> {

}
