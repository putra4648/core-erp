package id.putra.core_erp.service.excel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

/**
 * Verifies the formal layout contract of {@link ExcelSheetBuilder}.
 *
 * <p>Plain JUnit — no {@code @QuarkusTest}, because rendering a workbook touches no CDI or
 * database. That keeps this test fast enough to run on every build and means it works
 * without the dev-services Postgres container.
 */
class ExcelSheetBuilderTest {

	private static final List<ExcelColumn> COLUMNS = List.of(
			ExcelColumn.of("No.", 6, ExcelColumn.Type.INTEGER, HorizontalAlignment.CENTER),
			ExcelColumn.of("SKU", 18, ExcelColumn.Type.TEXT),
			ExcelColumn.of("Qty", 12, ExcelColumn.Type.INTEGER),
			ExcelColumn.of("Amount", 16, ExcelColumn.Type.CURRENCY),
			ExcelColumn.of("Date", 16, ExcelColumn.Type.DATETIME),
			ExcelColumn.of("Status", 14, ExcelColumn.Type.STATUS));

	private static ExcelReport sampleReport() {
		return new ExcelReport("Test Report",
				"Period: 2026-01-01 to 2026-01-31",
				COLUMNS,
				List.of(
						List.of(1, "SKU-1", 10, 1234.5, LocalDateTime.of(2026, 1, 5, 9, 30), "OK"),
						List.of(2, "SKU-2", 3, 99.0, LocalDateTime.of(2026, 1, 6, 14, 0), "LOW STOCK")));
	}

	private static Sheet renderToSheet(ExcelReport report) throws Exception {
		Workbook workbook = new XSSFWorkbook();
		ExcelStyles styles = new ExcelStyles(workbook);
		new ExcelSheetBuilder(workbook, styles).build("Sheet1", report, "ACME CORP");
		return workbook.getSheetAt(0);
	}

	@Test
	void writesDocumentHeaderBlock() throws Exception {
		Sheet sheet = renderToSheet(sampleReport());

		assertEquals("ACME CORP", sheet.getRow(0).getCell(0).getStringCellValue());
		// Title is uppercased by the builder regardless of the report's own casing.
		assertEquals("TEST REPORT", sheet.getRow(1).getCell(0).getStringCellValue());
		assertEquals("Period: 2026-01-01 to 2026-01-31", sheet.getRow(2).getCell(0).getStringCellValue());

		assertEquals(4, sheet.getNumMergedRegions(),
				"company, title, meta and footer lines must each be merged");
	}

	@Test
	void headerRowCarriesAnAutoFilter() throws Exception {
		Sheet sheet = renderToSheet(sampleReport());

		assertEquals("No.", sheet.getRow(4).getCell(0).getStringCellValue(), "header sits at row 4");
		// getCTWorksheet() is XSSF-only, hence the cast; the builder always writes XLSX.
		assertNotNull(((org.apache.poi.xssf.usermodel.XSSFSheet) sheet).getCTWorksheet().getAutoFilter(),
				"header must carry an autofilter");
	}

	@Test
	void dataCellsCarryTypedValuesNotStrings() throws Exception {
		Sheet sheet = renderToSheet(sampleReport());
		Row firstData = sheet.getRow(5);

		// Numbers stay numeric so the recipient can SUM and pivot them.
		assertEquals(CellType.NUMERIC, firstData.getCell(0).getCellType());
		assertEquals(1.0, firstData.getCell(0).getNumericCellValue());
		assertEquals(1234.5, firstData.getCell(3).getNumericCellValue());
		// Dates stay real dates, not pre-formatted strings.
		assertEquals(CellType.NUMERIC, firstData.getCell(4).getCellType());
		assertTrue(org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(firstData.getCell(4)));
	}

	@Test
	void totalRowSumsOnlyNumericColumns() throws Exception {
		Sheet sheet = renderToSheet(sampleReport());
		// rows: 0-2 header block, 3 spacer, 4 header, 5-6 data, 7 total
		Row total = sheet.getRow(7);

		// "No." is a counter, not a measure: the label anchors before "Qty", the first
		// totalled column, and the counter column itself stays empty.
		assertEquals("TOTAL", total.getCell(1).getStringCellValue());
		assertEquals(13.0, total.getCell(2).getNumericCellValue(), "quantities summed");
		assertEquals(1333.5, total.getCell(3).getNumericCellValue(), 0.001, "amounts summed");
		assertEquals(CellType.BLANK, total.getCell(0).getCellType(), "row counter must not be totalled");
		// The date column is not numeric, so it must not receive a total.
		assertEquals("", total.getCell(4).getStringCellValue());
	}

	@Test
	void emptyReportWritesNoticeAndNoTotal() throws Exception {
		Sheet sheet = renderToSheet(new ExcelReport("Empty", "", COLUMNS, List.of()));

		assertEquals("Tidak ada data untuk periode ini.", sheet.getRow(5).getCell(0).getStringCellValue());
		// A TOTAL row of zeros would read as real data, so an empty report must omit it.
		for (int i = 6; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			for (int c = 0; c < COLUMNS.size(); c++) {
				if (row.getCell(c) != null && row.getCell(c).getCellType() == CellType.STRING) {
					assertTrue(!"TOTAL".equals(row.getCell(c).getStringCellValue()),
							"empty report must not emit a TOTAL row");
				}
			}
		}
	}

	@Test
	void nullValuesBecomeBlankCellsNotZeros() throws Exception {
		Sheet sheet = renderToSheet(new ExcelReport("Nulls", "", COLUMNS,
				List.of(java.util.Arrays.asList(1, null, null, null, null, null))));
		Row data = sheet.getRow(5);

		assertEquals(CellType.BLANK, data.getCell(1).getCellType(), "null text must be blank");
		assertEquals(CellType.BLANK, data.getCell(2).getCellType(), "null number must be blank, not 0");
	}

	@Test
	void columnWidthsComeFromDefinitions() throws Exception {
		Sheet sheet = renderToSheet(sampleReport());

		for (int i = 0; i < COLUMNS.size(); i++) {
			assertEquals(COLUMNS.get(i).width() * 256, sheet.getColumnWidth(i), "width of column " + i);
		}
	}

	@Test
	void workbookRoundTripsThroughBytes() throws Exception {
		Workbook workbook = new XSSFWorkbook();
		new ExcelSheetBuilder(workbook, new ExcelStyles(workbook))
				.build("Sheet1", sampleReport(), "ACME CORP");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		workbook.write(baos);
		workbook.close();

		// Opening the produced bytes proves the file is a valid OOXML package.
		try (Workbook reopened = new XSSFWorkbook(new ByteArrayInputStream(baos.toByteArray()))) {
			assertEquals("ACME CORP", reopened.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
		}
	}

	@Test
	void dateOnlyColumnAcceptsLocalDate() throws Exception {
		List<ExcelColumn> cols = List.of(ExcelColumn.of("Date", 14, ExcelColumn.Type.DATE));
		ExcelReport report = new ExcelReport("Dates", "", cols, List.of(List.of(LocalDate.of(2026, 3, 4))));
		Sheet sheet = renderToSheet(report);

		assertTrue(org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(sheet.getRow(5).getCell(0)));
	}
}
