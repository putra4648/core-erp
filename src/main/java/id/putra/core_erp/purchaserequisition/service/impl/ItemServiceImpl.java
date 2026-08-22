package id.putra.core_erp.purchaserequisition.service.impl;

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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import id.putra.core_erp.purchaserequisition.constant.MessageConstant;
import id.putra.core_erp.purchaserequisition.dto.ItemDto;
import id.putra.core_erp.purchaserequisition.dto.SupplierDto;
import id.putra.core_erp.purchaserequisition.model.Item;
import id.putra.core_erp.purchaserequisition.model.ItemMeasurement;
import id.putra.core_erp.purchaserequisition.repository.ItemRepo;
import id.putra.core_erp.purchaserequisition.repository.SupplierRepo;
import id.putra.core_erp.purchaserequisition.service.ItemService;
import id.putra.core_erp.purchaserequisition.util.AppFileUtil;
import id.putra.core_erp.shared.entity.Supplier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("itemService")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepo itemRepo;
    private final SupplierRepo supplierRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addItem(ItemDto body, MultipartFile itemImage,
            List<MultipartFile> docs) throws Exception {
        Optional<Item> itemOptional = itemRepo.findById(body.getItem_number());
        if (!itemOptional.isPresent()) {
            Item entity = dtoToEntity(new Item(), body);
            handleImage(false, entity, itemImage, docs);
            itemRepo.save(entity);
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

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        Predicate statusLike = cb.like(cb.upper(root.get("status")), "%" + status.toUpperCase() + "%");
        Predicate nameLike = cb.like(cb.upper(root.get("name")), "%" + search.toUpperCase() + "%");
        Predicate itemNumberLike = cb.like(cb.upper(root.get("itemNumber")), "%" + search.toUpperCase() + "%");
        Predicate searchLike = cb.or(nameLike, itemNumberLike);
        Predicate supplierLike = cb.like(cb.upper(root.get("supplier")), "%" + supplierId + "%");
        TypedQuery<Item> itemsQuery = entityManager
                .createQuery(query.select(root).where(statusLike, searchLike, supplierLike));

        if (page != null && perPage != null) {
            int pageNum = page < 1 ? 0 : page - 1;
            Pageable pageable = PageRequest.of(pageNum, perPage, sort == null ? Sort.unsorted() : sort);
            itemsQuery.setFirstResult((int) pageable.getOffset());
            itemsQuery.setMaxResults(pageable.getPageSize());
        }

        return itemsQuery.getResultList().stream().map(this::entityItemToDto).toList();
    }

    @Override
    public void updateItem(ItemDto dto, MultipartFile itemImage,
            List<MultipartFile> itemDocs) throws Exception {
        if (dto.getItem_number() != null) {
            Optional<Item> itemOptional = itemRepo.findById(dto.getItem_number());
            if (itemOptional.isPresent()) {
                Item updatedEntity = dtoToEntity(itemOptional.get(), dto);
                handleImage(true, updatedEntity, itemImage, itemDocs);
                itemRepo.save(updatedEntity);
            } else {
                String message = MessageFormat.format(MessageConstant.NOT_FOUND, dto.getItem_number());
                throw new IllegalArgumentException(message);
            }
        } else {
            Optional<Item> item = itemRepo.findByNameEqualsIgnoreCase(dto.getName());
            if (item.isPresent()) {
                Item updatedEntity = dtoToEntity(item.get(), dto);
                handleImage(true, updatedEntity, itemImage, itemDocs);
                itemRepo.save(updatedEntity);
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

        if (body.getSupplier_id() != null && !body.getSupplier_id().isBlank()) {
            Optional<Supplier> supplierOpt = supplierRepo.findById(body.getSupplier_id());
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

    private void handleImage(boolean isUpdate, Item entity, MultipartFile itemImage,
            List<MultipartFile> docs) throws IOException {
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
                        log.error(e.getMessage(), e.getCause());
                    }
                });
                entity.setDocsPath(null);
            }
        }

        if (itemImage != null && !itemImage.isEmpty()) {
            path = AppFileUtil.createFileFromMultipartFile(entity.getItemNumber(), "items", itemImage);
            entity.setItemImagePath(path.toString());
        }

        if (docs != null) {
            List<String> docsPath = new ArrayList<>();
            docs.stream().forEach(data -> {
                try {
                    Path docPath = AppFileUtil.createFileFromMultipartFile(entity.getItemNumber(), "docs", data);
                    docsPath.add(docPath.toString());
                } catch (Exception e) {
                    log.error(e.getMessage(), e.getCause());
                }
            });
            entity.setDocsPath(docsPath);
        }
    }

    @Override
    public ItemDto getItemDetail(String id) {
        Optional<Item> itemOptional = itemRepo.findById(id);

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
    public void importItems(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String fileExtension = file.getOriginalFilename() != null
                ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))
                : "";
        if (!fileExtension.equalsIgnoreCase(".xlsx") && !fileExtension.equalsIgnoreCase(".xls")) {
            throw new IllegalArgumentException("Invalid file type. Please upload an Excel file.");
        }

        try (Workbook workbook = fileExtension.equalsIgnoreCase(".xlsx") ? new XSSFWorkbook(file.getInputStream())
                : new HSSFWorkbook(file.getInputStream())) {
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
                    Optional<Supplier> supplier = supplierRepo.findById(row.getCell(5).getStringCellValue());
                    supplier.ifPresent(item::setSupplier);
                }

                itemsToSave.add(item);
            }

            itemRepo.saveAll(itemsToSave);
        }
    }

    @Override
    public Resource exportItems() throws Exception {
        List<Item> items = StreamSupport.stream(itemRepo.findAll().spliterator(), false)
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
                return new ByteArrayResource(byteArrayOutputStream.toByteArray());
            }
        }
    }
}
