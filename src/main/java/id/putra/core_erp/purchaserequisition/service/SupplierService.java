package id.putra.core_erp.purchaserequisition.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import id.putra.core_erp.purchaserequisition.dto.SupplierDto;

public interface SupplierService {
    void addSupplier(SupplierDto body, MultipartFile logoFile) throws Exception;

    void updateSupplier(SupplierDto dto, MultipartFile logoFile) throws Exception;

    List<SupplierDto> getSupplier(
            String search,
            String status,
            Integer page,
            Integer perPage,
            String sortBy,
            String order) throws IOException;

    SupplierDto getSupplierInfo(String id);

    void updateSupplierStatus(Map<String, Object> body);

    List<SupplierDto> getSupplierDropdown();

    void importSuppliers(MultipartFile file) throws Exception;

    Resource exportSuppliers() throws Exception;
}

