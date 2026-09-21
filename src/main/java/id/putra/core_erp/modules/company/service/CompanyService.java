package id.putra.core_erp.modules.company.service;

import java.io.IOException;
import java.text.ParseException;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import id.putra.core_erp.modules.company.model.Company;

public interface CompanyService {
    void handleCompany(Company dto, FileUpload logoPath)
            throws ParseException, IOException;

    Company getProfile() throws IOException;
}
