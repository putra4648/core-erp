package id.putra.core_erp.modules.company.repository;

import jakarta.enterprise.context.ApplicationScoped;
import id.putra.core_erp.modules.company.model.Company;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class CompanyRepo implements PanacheRepository<Company> {

}
