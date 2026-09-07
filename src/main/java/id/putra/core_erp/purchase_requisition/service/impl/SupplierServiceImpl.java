package id.putra.core_erp.purchase_requisition.service.impl;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import id.putra.core_erp.purchase_requisition.dto.SupplierAddressDto;
import id.putra.core_erp.purchase_requisition.dto.SupplierContactDto;
import id.putra.core_erp.purchase_requisition.dto.SupplierDto;
import id.putra.core_erp.purchase_requisition.enums.StatusEnum;
import id.putra.core_erp.purchase_requisition.repository.SupplierRepo;
import id.putra.core_erp.purchase_requisition.service.SupplierService;
import id.putra.core_erp.shared.entity.Supplier;
import id.putra.core_erp.shared.entity.SupplierAddress;
import id.putra.core_erp.shared.entity.SupplierContact;
import id.putra.core_erp.shared.entity.SupplierPOAddress;
import io.quarkus.panache.common.Sort;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SupplierServiceImpl implements SupplierService {
    @Inject
    private SupplierRepo supplierRepo;

    @Override
    public void addSupplier(SupplierDto body, FileUpload logoFile)
            throws Exception {
        Optional<Supplier> supplier = supplierRepo.findById(body.getAcc_no());
        if (!supplier.isPresent()) {
            Supplier entity = dtoToEntity(new Supplier(), body);
            if (logoFile != null && !logoFile.isEmpty()) {
                Path path = AppFileUtil.createFileFromFileUpload(entity.getAccNo(), AppFileUtil.SUPPLIER_FOLDER,
                        logoFile);
                entity.setSupplierLogoPath(path.toString());
            }
            supplierRepo.persist(entity);
        } else {
            String message = MessageFormat.format(MessageConstant.EXIST, "Supplier ", body.getAcc_no());
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public List<SupplierDto> getSupplier(
            String search,
            String status,
            Integer page,
            Integer perPage,
            String sortBy,
            String order) {

        if (page == null || page < 1) {
            page = 1;
        }

        if (perPage == null || perPage < 1) {
            perPage = 100;
        }

        if (search == null) {
            search = "";
        }

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "name";
        }

        Sort sort = Sort.unsorted();
        if (order != null && !order.isBlank()) {
            sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        }

        if (status == null || status.isBlank()) {
            status = "";
        }

        return pageResult.getContent().stream().map(this::entityToDto).toList();
    }

    @Override
    public SupplierDto getSupplierInfo(String id) {
        Optional<Supplier> supplierOptional = supplierRepo.findById(id);

        if (supplierOptional.isPresent()) {
            return entityToDto(supplierOptional.get());
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Supplier " + id);
            throw new IllegalArgumentException(message);
        }
    }

    private Supplier dtoToEntity(Supplier entity, SupplierDto data) {
        entity.setName(data.getName());
        entity.setAccNo(data.getAcc_no());
        entity.setWebsite(data.getWebsite());
        if (data.getStatus() != null) {
            entity.setStatus(data.getStatus().getValue());
        }
        entity.setPhone(data.getPhone());
        entity.setFax(data.getFax());
        entity.setCurrency(data.getCurrency());
        entity.setNpwp(data.getNpwp());
        entity.setAccBank(data.getAcc_bank());
        entity.setNotes(data.getNotes());
        entity.setType(data.getType());

        if (data.getPo_address() != null) {
            SupplierPOAddress poAddress = entity.getSupplierPOAddress() != null ? entity.getSupplierPOAddress()
                    : new SupplierPOAddress();
            poAddress.setAddress(data.getPo_address().getAddress());
            poAddress.setCity(data.getPo_address().getCity());
            poAddress.setProvince(data.getPo_address().getProvince());
            poAddress.setZip(data.getPo_address().getZip());
            poAddress.setCountry(data.getPo_address().getCountry());
            entity.setSupplierPOAddress(poAddress);
        }

        if (data.getAddress() != null) {
            SupplierAddress address = entity.getSupplierAddress() != null ? entity.getSupplierAddress()
                    : new SupplierAddress();
            address.setAddress(data.getAddress().getAddress());
            address.setCity(data.getAddress().getCity());
            address.setProvince(data.getAddress().getProvince());
            address.setZip(data.getAddress().getZip());
            address.setCountry(data.getAddress().getCountry());
            entity.setSupplierAddress(address);
        }

        if (data.getContacts() != null && !data.getContacts().isEmpty()) {
            List<SupplierContact> contacts = new ArrayList<>();
            for (SupplierContactDto dto : data.getContacts()) {
                SupplierContact supContact = new SupplierContact();
                supContact.setName(dto.getName());
                supContact.setPhone(dto.getPhone());
                supContact.setEmail(dto.getEmail());
                supContact.setBranch(dto.getBranch());
                supContact.setSupplier(entity);
                contacts.add(supContact);
            }
            entity.setContacts(contacts);
        }

        return entity;
    }

    private SupplierDto entityToDto(Supplier data) {
        SupplierDto dto = new SupplierDto();
        dto.setId(data.getAccNo());
        dto.setName(data.getName());
        dto.setAcc_no(data.getAccNo());
        dto.setWebsite(data.getWebsite());
        if (data.getStatus() != null) {
            try {
                dto.setStatus(StatusEnum.valueOf(data.getStatus()));
            } catch (Exception ignored) {
                dto.setStatus(StatusEnum.ACTIVE);
            }
        }
        dto.setPhone(data.getPhone());
        dto.setFax(data.getFax());
        dto.setCurrency(data.getCurrency());
        dto.setNpwp(data.getNpwp());
        dto.setAcc_bank(data.getAccBank());
        if (data.getUpdatedDate() != null) {
            dto.setModified_date(Date.from(data.getUpdatedDate()));
        }

        if (data.getSupplierLogoPath() != null) {
            try {
                dto.setLogo_image(AppFileUtil.readFileToBase64(data.getSupplierLogoPath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dto.setType(data.getType());
        dto.setNotes(data.getNotes());

        if (data.getSupplierPOAddress() != null) {
            SupplierAddressDto poAddress = new SupplierAddressDto();
            poAddress.setId(data.getSupplierPOAddress().getId());
            poAddress.setAddress(data.getSupplierPOAddress().getAddress());
            poAddress.setCity(data.getSupplierPOAddress().getCity());
            poAddress.setProvince(data.getSupplierPOAddress().getProvince());
            poAddress.setZip(data.getSupplierPOAddress().getZip());
            poAddress.setCountry(data.getSupplierPOAddress().getCountry());
            dto.setPo_address(poAddress);
        }

        if (data.getSupplierAddress() != null) {
            SupplierAddressDto address = new SupplierAddressDto();
            address.setId(data.getSupplierAddress().getId());
            address.setAddress(data.getSupplierAddress().getAddress());
            address.setCity(data.getSupplierAddress().getCity());
            address.setProvince(data.getSupplierAddress().getProvince());
            address.setZip(data.getSupplierAddress().getZip());
            address.setCountry(data.getSupplierAddress().getCountry());
            dto.setAddress(address);
        }

        if (data.getContacts() != null) {
            List<SupplierContactDto> contactDtos = new ArrayList<>();
            data.getContacts().forEach(cnt -> {
                SupplierContactDto suppContactDto = new SupplierContactDto();
                suppContactDto.setId(cnt.getId());
                suppContactDto.setName(cnt.getName());
                suppContactDto.setEmail(cnt.getEmail());
                suppContactDto.setPhone(cnt.getPhone());
                suppContactDto.setBranch(cnt.getBranch());
                contactDtos.add(suppContactDto);
            });
            dto.setContacts(contactDtos);
        }
        return dto;
    }

    @Override
    public void updateSupplier(SupplierDto body, FileUpload logoFile)
            throws Exception {
        Optional<Supplier> supplier = supplierRepo.findById(body.getAcc_no());
        if (supplier.isPresent()) {
            Supplier entity = supplier.get();

            if (logoFile != null && !logoFile.isEmpty()) {
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
            Supplier updatedEntity = dtoToEntity(entity, body);
            supplierRepo.persist(updatedEntity);
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Supplier " + body.getAcc_no());
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void updateSupplierStatus(Map<String, Object> body) {
        String supplierId = (String) body.get("id");
        Optional<Supplier> supplier = supplierRepo.findById(supplierId);
        if (supplier.isPresent()) {
            Supplier entity = supplier.get();
            entity.setStatus(entity.getStatus().equalsIgnoreCase("ACTIVE") ? StatusEnum.INACTIVE.getValue()
                    : StatusEnum.ACTIVE.getValue());
            supplierRepo.persist(entity);
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Supplier ", supplierId);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public List<SupplierDto> getSupplierDropdown() {
        List<SupplierDto> dtos = new ArrayList<>();
        supplierRepo.findAllByStatus(StatusEnum.ACTIVE.getValue()).forEach(data -> dtos.add(entityToDto(data)));
        return dtos;
    }

    @Override
    public void importSuppliers(FileUpload file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.endsWith(".xlsx") && !originalFilename.endsWith(".xls"))) {
            throw new IllegalArgumentException("Invalid file type. Please upload an Excel file.");
        }

        try (Workbook workbook = originalFilename.endsWith(".xlsx")
                ? new XSSFWorkbook(file.getInputStream())
                : new HSSFWorkbook(file.getInputStream())) {

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
            supplier.setStatus(getCellValueAsString(row.getCell(3)).toUpperCase());
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
                cell3.setCellValue(supplier.getCountry() != null ? supplier.getCountry() : "");
                cell3.setCellStyle(cellStyle);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(supplier.getUpdatedDate() != null ? supplier.getUpdatedDate().toString() : "");
                cell4.setCellStyle(cellStyle);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                workbook.write(byteArrayOutputStream);
                return new ByteArrayResource(byteArrayOutputStream.toByteArray());
            }
        }
    }
}
