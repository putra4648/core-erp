package id.putra.core_erp.purchaserequisition.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import id.putra.core_erp.purchaserequisition.dto.CompanyDto;
import id.putra.core_erp.purchaserequisition.model.Company;
import id.putra.core_erp.purchaserequisition.repository.CompanyRepo;
import id.putra.core_erp.purchaserequisition.service.CompanyService;
import id.putra.core_erp.purchaserequisition.util.AppFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("companyService")
@RequiredArgsConstructor
@Transactional(rollbackFor = { IOException.class, ParseException.class })
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepo companyRepo;

    @Override
    public void handleCompany(CompanyDto dto, MultipartFile logo)
            throws ParseException, IOException {
        Optional<Company> comp = companyRepo.findById(dto.getId() != null ? dto.getId() : 1);
        Company company;

        if (comp.isPresent()) {
            company = comp.get();
            getCompany(company, dto, logo);
            companyRepo.save(company);
        } else {
            company = getCompany(new Company(), dto, logo);
            companyRepo.save(company);
        }
    }

    @Override
    public CompanyDto getProfile() throws IOException {
        return getCompanyDto();
    }

    private Company getCompany(Company company, CompanyDto dto, MultipartFile logo) throws ParseException, IOException {
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
        if (logo != null && !logo.isEmpty()) {
            try {
                Path logoPath = AppFileUtil.recreateFileFromMultipartFile(logo.getOriginalFilename(), logo, "data",
                        company.getCompLogopath());
                company.setCompLogopath(logoPath.toString());
            } catch (IOException e) {
                log.error(e.getMessage(), e.getCause());
            }
        }
        return company;
    }

    private CompanyDto getCompanyDto() throws IOException {
        Optional<Company> companyOpt = companyRepo.findById(1);
        if (companyOpt.isEmpty()) {
            return new CompanyDto();
        }
        Company company = companyOpt.get();

        CompanyDto dto = new CompanyDto();
        dto.setId(company.getCompId());
        dto.setName(company.getCompName());
        dto.setTelp(company.getCompTelp());
        dto.setNpwp(company.getCompNPWP());
        dto.setAccountBank(company.getCompAccbank());
        if (company.getCompLogopath() != null) {
            try {
                dto.setLogo_image(AppFileUtil.readFileToBase64(company.getCompLogopath()));
            } catch (IOException e) {
                log.error(e.getMessage(), e.getCause());
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

