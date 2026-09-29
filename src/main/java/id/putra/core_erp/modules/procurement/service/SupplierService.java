package id.putra.core_erp.modules.procurement.service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

import id.putra.core_erp.modules.procurement.repository.SupplierRepo;
import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.service.BaseService;
import id.putra.core_erp.service.ImportFIleService;
import io.quarkus.panache.common.Page;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SupplierService implements BaseService<Supplier>, ImportFIleService {

	private final SupplierRepo supplierRepo;

	public SupplierService(SupplierRepo supplierRepo) {
		this.supplierRepo = supplierRepo;
	}

	@Override
	public void delete(Long id) {
		supplierRepo.deleteById(id);
	}

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

				Cell cell2 = row.createCell(1);
				cell2.setCellValue(supplier.getPhone() != null ? supplier.getPhone() : "");
				cell2.setCellStyle(cellStyle);

				Cell cell3 = row.createCell(2);
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

	@Override
	public List<Supplier> findAll(Supplier entity, Page page) {
		return supplierRepo.find("name like ?1", "%" + entity.getName() + "%", page).list();
	}

	@Override
	public Supplier findById(Long id) {
		return supplierRepo.findById(id);
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
	public void importFile(FileUpload file) throws Exception {
		if (file == null) {
			throw new IllegalArgumentException("File is empty");
		}

		String originalFilename = file.fileName();
		if (originalFilename == null || (!originalFilename.endsWith(".xlsx") && !originalFilename.endsWith(".xls"))) {
			throw new IllegalArgumentException("Invalid file type. Please upload an Excel file.");
		}

		try (Workbook workbook = originalFilename.endsWith(".xlsx") ? new XSSFWorkbook() : new HSSFWorkbook()) {

			Sheet sheet = workbook.getSheetAt(0);
			List<Supplier> suppliersToSave = new ArrayList<>();

			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue;
				}

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

			supplier.setName(getCellValueAsString(row.getCell(1)));
			supplier.setPhone(getCellValueAsString(row.getCell(4)));

			if (supplier.getName().isEmpty()) {
				return null;
			}

			return supplier;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void save(Supplier entity) {
		supplierRepo.persist(entity);
	}

	@Override
	public void update(Supplier entity) {
		supplierRepo.update("name = ?1, phone = ?2, updateDate = ?3 where code = ?4", entity.getName(),
				entity.getPhone(), entity.getUpdatedDate(), entity.getCode());
	}
}
