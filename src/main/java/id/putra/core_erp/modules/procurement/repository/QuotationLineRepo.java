package id.putra.core_erp.modules.procurement.repository;

import id.putra.core_erp.modules.procurement.model.QuotationLine;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuotationLineRepo implements PanacheRepository<QuotationLine> {
}