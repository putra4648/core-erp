package id.putra.core_erp.modules.outbound.repository;

import id.putra.core_erp.modules.outbound.model.PickingTask;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PickingTaskRepository implements SoftDeleteRepository<PickingTask> {

}
