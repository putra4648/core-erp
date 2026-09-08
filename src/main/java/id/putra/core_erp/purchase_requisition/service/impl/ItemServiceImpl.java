package id.putra.core_erp.purchase_requisition.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import id.putra.core_erp.purchase_requisition.constant.MessageConstant;
import id.putra.core_erp.purchase_requisition.dto.ItemDto;
import id.putra.core_erp.purchase_requisition.dto.SupplierDto;
import id.putra.core_erp.purchase_requisition.model.Item;
import id.putra.core_erp.purchase_requisition.model.ItemMeasurement;
import id.putra.core_erp.purchase_requisition.repository.ItemRepo;
import id.putra.core_erp.purchase_requisition.repository.SupplierRepo;
import id.putra.core_erp.purchase_requisition.service.ItemService;
import id.putra.core_erp.shared.entity.Supplier;
import id.putra.core_erp.shared.util.AppFileUtil;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.vertx.mutiny.core.file.AsyncFile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class ItemServiceImpl implements ItemService {

    @Inject
    private ItemRepo itemRepo;
    @Inject
    private SupplierRepo supplierRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addItem(ItemDto body, FileUpload itemImage,
            List<FileUpload> docs) throws Exception {
        Optional<Item> itemOptional = itemRepo.findByNameEqualsIgnoreCase(body.getItem_number());
        if (!itemOptional.isPresent()) {
            Item entity = dtoToEntity(new Item(), body);
            handleImage(false, entity, itemImage, docs);
            itemRepo.persist(entity);
        } else {
            String message = MessageFormat.format(MessageConstant.EXIST, body.getItem_number());
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public List<ItemDto> getItems(String search, String status,
            String supplierId, Integer page, Integer perPage, String sortBy, String order) {
        if (search == null) {
            search = "";
        }

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "name";
        }

        if (status == null) {
            status = "";
        }

        if (supplierId == null) {
            supplierId = "";
        }

        Sort sort = null;

        if (order != null && !order.isBlank()) {
            if (order.equalsIgnoreCase("asc")) {
                sort = Sort.by(sortBy).ascending();
            } else {
                sort = Sort.by(sortBy).descending();
            }
        }

        var result = itemRepo.find("status like ?1 or name like ?2 or ", status).page(Page.ofSize(20));

        return result.list().stream().map(this::entityItemToDto).toList();
    }

    @Override
    public void updateItem(ItemDto dto, FileUpload itemImage,
            List<FileUpload> itemDocs) throws Exception {
        if (dto.getItem_number() != null) {
            Optional<Item> itemOptional = itemRepo.findByItemNumber(dto.getItem_number());
            if (itemOptional.isPresent()) {
                Item updatedEntity = dtoToEntity(itemOptional.get(), dto);
                handleImage(true, updatedEntity, itemImage, itemDocs);
                itemRepo.persist(updatedEntity);
            } else {
                String message = MessageFormat.format(MessageConstant.NOT_FOUND, dto.getItem_number());
                throw new IllegalArgumentException(message);
            }
        } else {
            Optional<Item> item = itemRepo.findByNameEqualsIgnoreCase(dto.getName());
            if (item.isPresent()) {
                Item updatedEntity = dtoToEntity(item.get(), dto);
                handleImage(true, updatedEntity, itemImage, itemDocs);
                itemRepo.persist(updatedEntity);
            } else {
                String message = MessageFormat.format(MessageConstant.NOT_FOUND, dto.getName());
                throw new IllegalArgumentException(message);
            }
        }
    }

    private SupplierDto entitySupperToDto(Supplier entity) {
        if (entity == null)
            return null;
        SupplierDto dto = new SupplierDto();
        dto.setAcc_no(entity.getAccNo());
        dto.setName(entity.getName());
        return dto;
    }

    private ItemDto entityItemToDto(Item entity) {
        ItemDto dto = new ItemDto();
        dto.setName(entity.getName());
        dto.setItem_number(entity.getItemNumber());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setSupplier(entitySupperToDto(entity.getSupplier()));
        dto.setGl_acc(entity.getGlAcc());
        if (entity.getUpdatedDate() != null) {
            dto.setUpdated_date(Date.from(entity.getUpdatedDate()));
        }
        if (entity.getCategory() != null && entity.getCategory().getId() != null) {
            dto.setCategory(entity.getCategory().getId().toString());
        }
        if (entity.getMeasurement() != null) {
            dto.setUnit(entity.getMeasurement().getUnit());
            dto.setNote(entity.getMeasurement().getNotes());
            dto.setAmount(entity.getMeasurement().getAmount());
            dto.setLabel(entity.getMeasurement().getUnit());
        }

        return dto;
    }

    private Item dtoToEntity(Item entity, ItemDto body) {
        entity.setName(body.getName());
        if (body.getItem_number() != null) {
            entity.setItemNumber(body.getItem_number());
        }
        entity.setPrice(body.getPrice());
        entity.setDescription(body.getDescription());
        if (body.getStatus() != null) {
            entity.setStatus(body.getStatus().toUpperCase());
        }

        if (body.getSupplier_id() != null) {
            Optional<Supplier> supplierOpt = supplierRepo.findByIdOptional(body.getSupplier_id());
            supplierOpt.ifPresent(entity::setSupplier);
        }
        entity.setGlAcc(body.getGl_acc());

        ItemMeasurement measurement = entity.getMeasurement() != null ? entity.getMeasurement() : new ItemMeasurement();
        measurement.setUnit(body.getUnit());
        measurement.setAmount(body.getAmount());
        measurement.setNotes(body.getNote());
        measurement.setItem(entity);

        entity.setMeasurement(measurement);

        return entity;
    }

    private void handleImage(boolean isUpdate, Item entity, FileUpload itemImage,
            List<FileUpload> docs) throws IOException {
        Path path = null;

        if (isUpdate) {
            String itemImagePath = entity.getItemImagePath();
            List<String> docsPath = entity.getDocsPath();

            if (itemImagePath != null) {
                try {
                    path = Paths.get(itemImagePath);
                    Files.deleteIfExists(path);
                } catch (Exception ignored) {
                }
                entity.setItemImagePath(null);
            }

            if (docsPath != null) {
                docsPath.forEach(docPath -> {
                    try {
                        Files.deleteIfExists(Paths.get(docPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                entity.setDocsPath(null);
            }
        }

        if (itemImage != null) {
            path = AppFileUtil.createFileFromFileUpload(entity.getItemNumber(), "items", itemImage);
            entity.setItemImagePath(path.toString());
        }

        if (docs != null) {
            List<String> docsPath = new ArrayList<>();
            docs.stream().forEach(data -> {
                try {
                    Path docPath = AppFileUtil.createFileFromFileUpload(entity.getItemNumber(), "docs", data);
                    docsPath.add(docPath.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            entity.setDocsPath(docsPath);
        }
    }

    @Override
    public ItemDto getItemDetail(Long id) {
        Optional<Item> itemOptional = itemRepo.findByIdOptional(id);

        if (itemOptional.isPresent()) {
            Item entity = itemOptional.get();
            return entityItemToDto(entity);
        } else {
            String message = MessageFormat.format(MessageConstant.NOT_FOUND, "Item " + id);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public Boolean isItemExist(String name) {
        return itemRepo.findByNameEqualsIgnoreCase(name).isPresent();
    }

    @Override
    public void importItems(FileUpload file) throws Exception {
        if (file == null) {
            throw new IllegalArgumentException("File is empty");
        }

        String fileExtension = file.fileName() != null
                ? file.fileName().substring(file.fileName().lastIndexOf("."))
                : "";
        if (!fileExtension.equalsIgnoreCase(".xlsx") && !fileExtension.equalsIgnoreCase(".xls")) {
            throw new IllegalArgumentException("Invalid file type. Please upload an Excel file.");
        }

        try (Workbook workbook = fileExtension.equalsIgnoreCase(".xlsx") ? new XSSFWorkbook()
                : new HSSFWorkbook()) {
            Sheet sheet = workbook.getSheetAt(0);

            List<Item> itemsToSave = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                Item item = new Item();
                item.setItemNumber(row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "");
                item.setName(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "");
                item.setPrice(BigInteger
                        .valueOf((long) (row.getCell(2) != null ? row.getCell(2).getNumericCellValue() * 100 : 0)));
                item.setDescription(row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "");
                item.setStatus(row.getCell(4) != null ? row.getCell(4).getStringCellValue().toUpperCase() : "ACTIVE");

                if (row.getCell(5) != null) {
                    Optional<Supplier> supplier = supplierRepo
                            .findByIdOptional(Long.parseLong(row.getCell(5).getStringCellValue()));
                    supplier.ifPresent(item::setSupplier);
                }

                itemsToSave.add(item);
            }

            itemRepo.persist(itemsToSave);
        }
    }

    @Override
    public AsyncFile exportItems() throws Exception {
        List<Item> items = StreamSupport.stream(itemRepo.findAll().list().spliterator(), false)
                .collect(Collectors.toList());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Items");

            String[] headers = { "Item Name", "Item Number", "Purchase Price", "U.O.M", "Last Edited" };
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
            for (Item item : items) {
                Row row = sheet.createRow(rowNum++);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(item.getName());
                cell0.setCellStyle(cellStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(item.getItemNumber());
                cell1.setCellStyle(cellStyle);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(item.getPrice() != null ? item.getPrice().doubleValue() / 100 : 0.0);
                cell2.setCellStyle(cellStyle);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(item.getMeasurement() != null ? item.getMeasurement().getUnit() : "");
                cell3.setCellStyle(cellStyle);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(item.getUpdatedDate() != null ? item.getUpdatedDate().toString() : "");
                cell4.setCellStyle(cellStyle);
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
