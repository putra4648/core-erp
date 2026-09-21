package id.putra.core_erp.modules.outbound.repository;

import id.putra.core_erp.modules.outbound.model.PickingTask;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PickingTaskRepository implements PanacheRepositoryBase<PickingTask, Long> {

}
