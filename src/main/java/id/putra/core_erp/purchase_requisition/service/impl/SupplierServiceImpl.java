package id.putra.core_erp.purchase_requisition.service.impl;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import id.putra.core_erp.purchase_requisition.constant.MessageConstant;
import id.putra.core_erp.purchase_requisition.enums.StatusEnum;
import id.putra.core_erp.purchase_requisition.repository.SupplierRepo;
import id.putra.core_erp.purchase_requisition.service.SupplierService;
import id.putra.core_erp.shared.entity.Supplier;
import id.putra.core_erp.shared.util.AppFileUtil;
import io.quarkus.panache.common.Page;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepo;

    public SupplierServiceImpl(SupplierRepo supplierRepo) {
        this.supplierRepo = supplierRepo;
    }

    @Override
    public void addSupplier(Supplier body, FileUpload logoFile)
            throws Exception {
        Optional<Supplier> supplier = supplierRepo.findByAccNo(body.getAccNo());
        if (!supplier.isPresent()) {
            Supplier entity = new Supplier();
            entity.setAccNo(body.getAccNo());
            entity.setName(body.getName());
            entity.setWebsite(body.getWebsite());
            entity.setStatus(body.getStatus());
            entity.setPhone(body.getPhone());
            entity.setFax(body.getFax());
            entity.setCurrency(body.getCurrency());
            entity.setNpwp(body.getNpwp());
            entity.setAccBank(body.getAccBank());
            entity.setNotes(body.getNotes());
            entity.setType(body.getType());

            if (logoFile != null) {
                Path path = AppFileUtil.createFileFromFileUpload(entity.getAccNo(), AppFileUtil.SUPPLIER_FOLDER,
                        logoFile);
                entity.setSupplierLogoPath(path.toString());
            }
            supplierRepo.persist(entity);
        } else {
            String message = MessageFormat.format(MessageConstant.EXIST, "Supplier ", body.getAccNo());
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public List<Supplier> getSupplier(String search, String status, Page page) {

        if (status == null || status.isBlank()) {
            status = "";
        }

        var result = supplierRepo.findAll().page(page);

        return result.list();
    }

    @Override
    public Supplier getSupplier(Long id) {
        Optional<Supplier> supplierOptional = supplierRepo.findByIdOptional(id);

        if (supplierOptional.isPresent()) {
            return supplierOptional.get();
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Supplier " + id);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void updateSupplier(Supplier body, FileUpload logoFile)
            throws Exception {
        Optional<Supplier> supplier = supplierRepo.findByAccNo(body.getAccNo());
        if (supplier.isPresent()) {
            Supplier entity = supplier.get();

            if (logoFile != null) {
                if (entity.getSupplierLogoPath() != null) {
                    Path path = AppFileUtil.recreateFileFromFileUpload(entity.getAccNo(), logoFile,
                            AppFileUtil.SUPPLIER_FOLDER,
                            entity.getSupplierLogoPath());
                    entity.setSupplierLogoPath(path.toString());
                } else {
                    Path path = AppFileUtil.createFileFromFileUpload(entity.getAccNo(), AppFileUtil.SUPPLIER_FOLDER,
                            logoFile);
                    entity.setSupplierLogoPath(path.toString());
                }
            }

            entity.setName(body.getName());
            entity.setAccNo(body.getAccNo());
            entity.setWebsite(body.getWebsite());
            entity.setStatus(body.getStatus());
            entity.setPhone(body.getPhone());
            entity.setFax(body.getFax());
            entity.setCurrency(body.getCurrency());
            entity.setNpwp(body.getNpwp());
            entity.setAccBank(body.getAccBank());
            entity.setNotes(body.getNotes());
            entity.setType(body.getType());

            Supplier updatedEntity = entity;
            supplierRepo.persist(updatedEntity);
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Supplier " + body.getAccNo());
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void updateSupplierStatus(Map<String, Object> body) {
        Long id = (Long) body.get("id");
        Optional<Supplier> supplier = supplierRepo.findByIdOptional(id);
        if (supplier.isPresent()) {
            Supplier entity = supplier.get();
            entity.setStatus(entity.getStatus().equals(StatusEnum.ACTIVE) ? StatusEnum.INACTIVE
                    : StatusEnum.ACTIVE);
            supplierRepo.persist(entity);
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Supplier ", id);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public List<Supplier> getSupplierDropdown() {
        return supplierRepo.findByStatus(StatusEnum.ACTIVE);
    }

    @Override
    public void importSuppliers(FileUpload file) throws Exception {
        if (file == null) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalFilename = file.fileName();
        if (originalFilename == null || (!originalFilename.endsWith(".xlsx") && !originalFilename.endsWith(".xls"))) {
            throw new IllegalArgumentException("Invalid file type. Please upload an Excel file.");
        }

        try (Workbook workbook = originalFilename.endsWith(".xlsx")
                ? new XSSFWorkbook()
                : new HSSFWorkbook()) {

            Sheet sheet = workbook.getSheetAt(0);
            List<Supplier> suppliersToSave = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                Supplier supplier = parseRowToSupplier(row);
                if (supplier != null) {
                    suppliersToSave.add(supplier);
                }
            }

            if (!suppliersToSave.isEmpty()) {
                supplierRepo.persist(suppliersToSave);
            } else {
                throw new IllegalArgumentException("No valid suppliers found in the file.");
            }
        }
    }

    private Supplier parseRowToSupplier(Row row) {
        try {
            Supplier supplier = new Supplier();

            supplier.setAccNo(getCellValueAsString(row.getCell(0)));
            supplier.setName(getCellValueAsString(row.getCell(1)));
            supplier.setWebsite(getCellValueAsString(row.getCell(2)));
            supplier.setStatus(StatusEnum.valueOf(getCellValueAsString(row.getCell(3))));
            supplier.setPhone(getCellValueAsString(row.getCell(4)));
            supplier.setFax(getCellValueAsString(row.getCell(5)));
            supplier.setCurrency(getCellValueAsString(row.getCell(6)));
            supplier.setNpwp(getCellValueAsString(row.getCell(7)));
            supplier.setAccBank(getCellValueAsString(row.getCell(8)));
            supplier.setNotes(getCellValueAsString(row.getCell(9)));

            if (supplier.getAccNo().isEmpty() || supplier.getName().isEmpty()) {
                return null;
            }

            return supplier;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    return dateFormat.format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue()).trim();
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()).trim();
            case FORMULA:
                return cell.getCellFormula().trim();
            default:
                return "";
        }
    }

    @Override
    public Resource exportSuppliers() throws Exception {
        List<Supplier> suppliers = StreamSupport.stream(supplierRepo.findAll().list().spliterator(), false)
                .collect(Collectors.toList());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Suppliers");

            String[] headers = { "Supplier Name", "Main Contact", "Phone Number", "Country", "Last Edited" };
            Row headerRow = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Supplier supplier : suppliers) {
                Row row = sheet.createRow(rowNum++);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(supplier.getName() != null ? supplier.getName() : "");
                cell0.setCellStyle(cellStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(supplier.getMainContact() != null ? supplier.getMainContact() : "");
                cell1.setCellStyle(cellStyle);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(supplier.getPhone() != null ? supplier.getPhone() : "");
                cell2.setCellStyle(cellStyle);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(supplier.getUpdatedDate() != null ? supplier.getUpdatedDate().toString() : "");
                cell3.setCellStyle(cellStyle);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                workbook.write(byteArrayOutputStream);
                return null;
            }
        }
    }
}
