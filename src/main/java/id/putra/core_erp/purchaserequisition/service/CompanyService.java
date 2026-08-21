package id.putra.core_erp.purchaserequisition.service;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.web.multipart.MultipartFile;
import id.putra.core_erp.purchaserequisition.dto.CompanyDto;

public interface CompanyService {
    void handleCompany(CompanyDto dto, MultipartFile logoPath)
            throws ParseException, IOException;

    CompanyDto getProfile() throws IOException;
}

