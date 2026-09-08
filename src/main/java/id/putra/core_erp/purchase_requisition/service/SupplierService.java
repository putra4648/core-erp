package id.putra.core_erp.purchase_requisition.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import id.putra.core_erp.shared.entity.Supplier;
import io.quarkus.panache.common.Page;
import jakarta.annotation.Resource;

public interface SupplierService {
    void addSupplier(Supplier body, FileUpload logoFile) throws Exception;

    void updateSupplier(Supplier dto, FileUpload logoFile) throws Exception;

    List<Supplier> getSupplier(
            String search,
            String status, Page page) throws IOException;

    Supplier getSupplier(Long id);

    void updateSupplierStatus(Map<String, Object> body);

    List<Supplier> getSupplierDropdown();

    void importSuppliers(FileUpload file) throws Exception;

    Resource exportSuppliers() throws Exception;
}
