package id.putra.core_erp.modules.procurement.repository;

import id.putra.core_erp.modules.procurement.model.QuotationLine;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuotationLineRepo implements SoftDeleteRepository<QuotationLine> {
}