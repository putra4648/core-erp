package id.putra.core_erp.purchase_requisition.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import id.putra.core_erp.purchase_requisition.dto.CompanyDto;
import id.putra.core_erp.purchase_requisition.model.Company;
import id.putra.core_erp.purchase_requisition.repository.CompanyRepo;
import id.putra.core_erp.purchase_requisition.service.CompanyService;
import id.putra.core_erp.shared.util.AppFileUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CompanyServiceImpl implements CompanyService {

    @Inject
    private CompanyRepo companyRepo;

    @Override
    public void handleCompany(CompanyDto dto, FileUpload logo)
            throws ParseException, IOException {
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
    }

    @Override
    public CompanyDto getProfile() throws IOException {
        return getCompanyDto();
    }

    private Company getCompany(Company company, CompanyDto dto, FileUpload logo) throws ParseException, IOException {
        company.setCompName(dto.getName());
        company.setCompTelp(dto.getTelp());
        company.setCompNPWP(dto.getNpwp());
        company.setCompAccbank(dto.getAccountBank());
        if (dto.getFinancialEndYear() != null && !dto.getFinancialEndYear().isBlank()) {
            company.setCompEndyear(new SimpleDateFormat("yyyy-MM-dd").parse(dto.getFinancialEndYear()));
        }

        List<String> currs = new ArrayList<>();
        currs.add(dto.getCurr1());
        currs.add(dto.getCurr2());
        currs.add(dto.getCurr3());
        company.setCompCurrencies(currs);
        company.setCompNeedAutoCloseOrder(dto.getIsAutoCloseOrder());
        company.setCompNeedRegisterApproval(dto.getIsRegisterApproval());
        if (logo != null) {
            try {
                Path logoPath = AppFileUtil.recreateFileFromFileUpload(logo.fileName(), logo, "data",
                        company.getCompLogopath());
                company.setCompLogopath(logoPath.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return company;
    }

    private CompanyDto getCompanyDto() throws IOException {
        Optional<Company> companyOpt = companyRepo.findByIdOptional(1L);
        if (companyOpt.isEmpty()) {
            return new CompanyDto();
        }
        Company company = companyOpt.get();

        CompanyDto dto = new CompanyDto();
        dto.setId(company.getId());
        dto.setName(company.getCompName());
        dto.setTelp(company.getCompTelp());
        dto.setNpwp(company.getCompNPWP());
        dto.setAccountBank(company.getCompAccbank());
        if (company.getCompLogopath() != null) {
            try {
                dto.setLogo_image(AppFileUtil.readFileToBase64(company.getCompLogopath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (company.getCompEndyear() != null) {
            dto.setFinancialEndYear(new SimpleDateFormat("yyyy-MM-dd").format(company.getCompEndyear()));
        }
        if (company.getCompCurrencies() != null && !company.getCompCurrencies().isEmpty()) {
            dto.setCurr1(company.getCompCurrencies().size() > 0 ? company.getCompCurrencies().get(0) : "IDR");
            dto.setCurr2(company.getCompCurrencies().size() > 1 ? company.getCompCurrencies().get(1) : "USD");
            dto.setCurr3(company.getCompCurrencies().size() > 2 ? company.getCompCurrencies().get(2) : "EUR");
        }
        dto.setIsAutoCloseOrder(company.getCompNeedAutoCloseOrder());
        dto.setIsRegisterApproval(company.getCompNeedRegisterApproval());
        return dto;
    }

}
