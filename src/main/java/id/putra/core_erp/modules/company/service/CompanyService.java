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

<<<<<<< HEAD
	private Company getCompany() throws IOException {
		Optional<Company> companyOpt = companyRepo.findByIdOptional(1L);
		if (companyOpt.isEmpty()) {
			return new Company();
		}
		Company company = companyOpt.get();

		Company dto = new Company();
		dto.setId(company.getId());
		dto.setName(company.getName());
		dto.setTelp(company.getTelp());
		dto.setNpwp(company.getNpwp());
		dto.setAccountBank(company.getAccountBank());
		if (company.getLogoPath() != null) {
			try {
				dto.setLogoPath(AppFileUtil.readFileToBase64(company.getLogoPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (company.getFinancialEndYear() != null) {
			dto.setFinancialEndYear(company.getFinancialEndYear());
		}
		dto.setCurr(company.getCurr());
		dto.setNeedAutoCloseOrder(company.getNeedAutoCloseOrder());
		dto.setNeedRegisterApproval(company.getNeedRegisterApproval());
		return dto;
	}

	private Company getCompany(Company company, Company dto, FileUpload logo) throws ParseException, IOException {
		company.setName(dto.getName());
		company.setTelp(dto.getTelp());
		company.setNpwp(dto.getNpwp());
		company.setAccountBank(dto.getAccountBank());
		if (dto.getFinancialEndYear() != null) {
			company.setFinancialEndYear(dto.getFinancialEndYear());
		}

		company.setCurr(dto.getCurr());
		company.setNeedAutoCloseOrder(dto.getNeedAutoCloseOrder());
		company.setNeedRegisterApproval(dto.getNeedRegisterApproval());
		if (logo != null) {
			try {
				Path logoPath = AppFileUtil.recreateFileFromFileUpload(logo.fileName(), logo, "data",
						company.getLogoPath());
				company.setLogoPath(logoPath.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return company;
	}

	public Company getProfile() throws IOException {
		return getCompany();
	}

	public void handleCompany(Company dto, FileUpload logo) throws ParseException, IOException {
		Optional<Company> comp = companyRepo.findByIdOptional(dto.getId());
		Company company;

		if (comp.isPresent()) {
			company = comp.get();
			getCompany(company, dto, logo);
			companyRepo.persist(company);
		} else {
			company = getCompany(new Company(), dto, logo);
			companyRepo.persist(company);
		}
=======
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
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

}
