package id.putra.core_erp.purchase_requisition.repository;

import id.putra.core_erp.purchase_requisition.model.RFQ;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RFQRepo implements PanacheRepository<RFQ> {

}
