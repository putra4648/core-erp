package id.putra.core_erp.purchase_requisition.service;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.web.multipart.MultipartFile;

import id.putra.core_erp.purchase_requisition.dto.CompanyDto;

public interface CompanyService {
    void handleCompany(CompanyDto dto, MultipartFile logoPath)
            throws ParseException, IOException;

    CompanyDto getProfile() throws IOException;
}
