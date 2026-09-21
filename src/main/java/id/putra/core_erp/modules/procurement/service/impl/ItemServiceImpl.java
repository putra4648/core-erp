package id.putra.core_erp.modules.procurement.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
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

import id.putra.core_erp.modules.procurement.model.Item;
import id.putra.core_erp.modules.procurement.repository.ItemRepo;
import id.putra.core_erp.modules.procurement.repository.SupplierRepo;
import id.putra.core_erp.modules.procurement.service.ItemService;
import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.shared.constant.MessageConstant;
import id.putra.core_erp.shared.util.AppFileUtil;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class ItemServiceImpl implements ItemService {

    private final ItemRepo itemRepo;
    private final SupplierRepo supplierRepo;

    public ItemServiceImpl(ItemRepo itemRepo, SupplierRepo supplierRepo) {
        this.itemRepo = itemRepo;
        this.supplierRepo = supplierRepo;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addItem(Item body, FileUpload itemImage,
            List<FileUpload> docs) throws Exception {
        Optional<Item> itemOptional = itemRepo.findByNameEqualsIgnoreCase(body.getItemNumber());
        if (!itemOptional.isPresent()) {
            Item entity = body;
            handleImage(false, entity, itemImage, docs);
            itemRepo.persist(entity);
        } else {
            String message = MessageFormat.format(MessageConstant.EXIST, body.getItemNumber());
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public List<Item> getItems(String search, String status,
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

        var result = itemRepo.find("status like ?1 or name like ?2 or ", status).page(Page.ofSize(20));

        return result.list().stream().toList();
    }

    @Override
    public void updateItem(Item dto, FileUpload itemImage,
            List<FileUpload> itemDocs) throws Exception {
        if (dto.getItemNumber() != null) {
            Optional<Item> itemOptional = itemRepo.findByItemNumber(dto.getItemNumber());
            if (itemOptional.isPresent()) {
                Item updatedEntity = itemOptional.get();
                updatedEntity.setName(dto.getName());
                updatedEntity.setPrice(dto.getPrice());
                updatedEntity.setDescription(dto.getDescription());
                updatedEntity.setStatus(dto.getStatus());
                updatedEntity.setSupplier(dto.getSupplier());
                updatedEntity.setGlAcc(dto.getGlAcc());
                handleImage(true, updatedEntity, itemImage, itemDocs);
                itemRepo.persist(updatedEntity);
            } else {
                String message = MessageFormat.format(MessageConstant.NOT_FOUND, dto.getItemNumber());
                throw new IllegalArgumentException(message);
            }
        } else {
            Optional<Item> item = itemRepo.findByNameEqualsIgnoreCase(dto.getName());
            if (item.isPresent()) {
                Item updatedEntity = dto;
                handleImage(true, updatedEntity, itemImage, itemDocs);
                itemRepo.persist(updatedEntity);
            } else {
                String message = MessageFormat.format(MessageConstant.NOT_FOUND, dto.getName());
                throw new IllegalArgumentException(message);
            }
        }
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
    public Item getItemDetail(Long id) {
        Optional<Item> itemOptional = itemRepo.findByIdOptional(id);

        if (itemOptional.isPresent()) {
            Item entity = itemOptional.get();
            return entity;
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
    public byte[] exportItems() throws Exception {
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
                cell4.setCellValue(item.getUpdateAt() != null ? item.getUpdateAt().toString() : "");
                cell4.setCellStyle(cellStyle);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        }
    }
}
