package id.putra.core_erp.modules.inbound.repository;

import id.putra.core_erp.modules.inbound.model.ReceivingLine;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReceivingLineRepository implements SoftDeleteRepository<ReceivingLine> {

}
