package id.putra.core_erp.modules.company.service;

import id.putra.core_erp.modules.company.model.Company;
import id.putra.core_erp.modules.company.repository.CompanyRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CompanyService implements BaseService<Company> {

	private final CompanyRepo companyRepo;

	public CompanyService(CompanyRepo companyRepo) {
		this.companyRepo = companyRepo;
	}

	@Override
	public void save(Company entity) {
		companyRepo.persist(entity);
	}

	@Override
	public Company findById(Long id) {
		return companyRepo.findById(id);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Company entity) {
		companyRepo.getEntityManager().merge(entity);

	}

	@Override
	public Iterable<Company> findAll(Company entity, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

}
