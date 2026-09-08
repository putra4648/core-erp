package id.putra.core_erp.purchase_requisition.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import id.putra.core_erp.purchase_requisition.dto.SupplierDto;
import jakarta.annotation.Resource;

public interface SupplierService {
    void addSupplier(SupplierDto body, FileUpload logoFile) throws Exception;

    void updateSupplier(SupplierDto dto, FileUpload logoFile) throws Exception;

    List<SupplierDto> getSupplier(
            String search,
            String status,
            Integer page,
            Integer perPage,
            String sortBy,
            String order) throws IOException;

    SupplierDto getSupplier(Long id);

    void updateSupplierStatus(Map<String, Object> body);

    List<SupplierDto> getSupplierDropdown();

    void importSuppliers(FileUpload file) throws Exception;

    Resource exportSuppliers() throws Exception;
}
