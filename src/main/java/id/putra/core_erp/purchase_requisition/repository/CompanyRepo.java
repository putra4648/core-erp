package id.putra.core_erp.purchase_requisition.repository;

import jakarta.enterprise.context.ApplicationScoped;
import id.putra.core_erp.purchase_requisition.model.Company;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class CompanyRepo implements PanacheRepository<Company> {

}
