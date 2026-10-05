package id.putra.core_erp.modules.company.repository;

import id.putra.core_erp.modules.company.model.Company;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CompanyRepo implements PanacheRepository<Company> {

}
