package id.putra.core_erp.service.excel;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Central registry of cell styles for formal Excel reports.
 *
 * <p>One instance per {@link Workbook}. POI requires every {@link CellStyle} to be created
 * from the workbook that uses it, and reusing a style across workbooks corrupts the file —
 * so this type is deliberately not a singleton.
 *
 * <p>Styles are built once in the constructor rather than on demand. POI caps a workbook at
 * 64,000 distinct styles; this class produces ~20, which is negligible, and eager creation
 * means a typo in a format string fails at construction instead of silently mid-report.
 *
 * <p>Two conventions run through the definitions:
 * <ul>
 *   <li>Zebra striping — every data style has an {@code Alt} counterpart used on even rows.</li>
 *   <li>Borders are on data and header cells only. Borders on title/footer cells would draw
 *       a box around the heading block, which reads as a table rather than a document header.</li>
 * </ul>
 */
public final class ExcelStyles {

	private static final short TITLE_POINTS = 16;
	private static final short COMPANY_POINTS = 14;
	private static final short HEADER_POINTS = 11;
	private static final short BODY_POINTS = 10;
	private static final short META_POINTS = 10;
	private static final short FOOTER_POINTS = 8;

	private static final String FMT_INTEGER = "#,##0";
	private static final String FMT_DECIMAL = "#,##0.00";
	private static final String FMT_DATE = "yyyy-mm-dd";
	private static final String FMT_DATETIME = "yyyy-mm-dd hh:mm";

	private final CellStyle company;
	private final CellStyle title;
	private final CellStyle meta;
	private final CellStyle header;
	private final CellStyle footer;
	private final CellStyle totalLabel;
	private final CellStyle totalInteger;
	private final CellStyle totalDecimal;

	private final Map<ExcelColumn.Type, CellStyle> body;
	private final Map<ExcelColumn.Type, CellStyle> bodyAlt;
	private final Map<String, CellStyle> status;

	public ExcelStyles(Workbook workbook) {
		this.company = buildCompany(workbook);
		this.title = buildTitle(workbook);
		this.meta = buildMeta(workbook);
		this.header = buildHeader(workbook);
		this.footer = buildFooter(workbook);
		this.totalLabel = buildTotalLabel(workbook);
		this.totalInteger = buildTotal(workbook, FMT_INTEGER);
		this.totalDecimal = buildTotal(workbook, FMT_DECIMAL);

		this.body = buildBodyStyles(workbook, false);
		this.bodyAlt = buildBodyStyles(workbook, true);
		this.status = buildStatusStyles(workbook);
	}

	// ---------------------------------------------------------------- public API

	public CellStyle company() {
		return company;
	}

	public CellStyle title() {
		return title;
	}

	public CellStyle meta() {
		return meta;
	}

	public CellStyle header() {
		return header;
	}

	public CellStyle footer() {
		return footer;
	}

	public CellStyle totalLabel() {
		return totalLabel;
	}

	/**
	 * Style for a data cell.
	 *
	 * @param type  the column type
	 * @param zebra {@code true} for the shaded variant used on even rows
	 * @param value the cell value, used only to pick a colour for {@link ExcelColumn.Type#STATUS}
	 * @return the style to apply
	 */
	public CellStyle body(ExcelColumn.Type type, boolean zebra, Object value) {
		if (type == ExcelColumn.Type.STATUS) {
			return status.getOrDefault(statusKey(value), status.get("__default"));
		}
		return (zebra ? bodyAlt : body).get(type);
	}

	/** Style for a cell in the total row, chosen by the column's numeric format. */
	public CellStyle totalFor(ExcelColumn column) {
		return column.type() == ExcelColumn.Type.INTEGER ? totalInteger : totalDecimal;
	}

	// ---------------------------------------------------------------- builders

	private static CellStyle buildCompany(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints(COMPANY_POINTS);
		font.setColor(IndexedColors.DARK_BLUE.getIndex());
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}

	private static CellStyle buildTitle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints(TITLE_POINTS);
		font.setColor(IndexedColors.BLACK.getIndex());
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}

	private static CellStyle buildMeta(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setItalic(true);
		font.setFontHeightInPoints(META_POINTS);
		font.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}

	private static CellStyle buildHeader(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints(HEADER_POINTS);
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		applyBorder(style, BorderStyle.THIN);
		return style;
	}

	private static CellStyle buildFooter(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setItalic(true);
		font.setFontHeightInPoints(FOOTER_POINTS);
		font.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.LEFT);
		return style;
	}

	private static CellStyle buildTotalLabel(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints(BODY_POINTS);
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		applyBorder(style, BorderStyle.THIN);
		style.setBorderTop(BorderStyle.MEDIUM);
		return style;
	}

	private static CellStyle buildTotal(Workbook wb, String format) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints(BODY_POINTS);
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setDataFormat(wb.createDataFormat().getFormat(format));
		applyBorder(style, BorderStyle.THIN);
		style.setBorderTop(BorderStyle.MEDIUM);
		return style;
	}

	private static Map<ExcelColumn.Type, CellStyle> buildBodyStyles(Workbook wb, boolean zebra) {
		Map<ExcelColumn.Type, CellStyle> styles = new HashMap<>();
		for (ExcelColumn.Type type : ExcelColumn.Type.values()) {
			if (type == ExcelColumn.Type.STATUS) {
				continue;
			}
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontHeightInPoints(BODY_POINTS);
			style.setFont(font);
			if (zebra) {
				style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			}
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			style.setAlignment(ExcelColumn.of("", 0, type).effectiveAlignment());
			String format = formatFor(type);
			if (format != null) {
				style.setDataFormat(wb.createDataFormat().getFormat(format));
			}
			applyBorder(style, BorderStyle.THIN);
			styles.put(type, style);
		}
		return styles;
	}

	private static Map<String, CellStyle> buildStatusStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<>();
		styles.put("__default", buildStatus(wb, IndexedColors.BLACK));
		styles.put("ok", buildStatus(wb, IndexedColors.GREEN));
		styles.put("low stock", buildStatus(wb, IndexedColors.RED));
		styles.put("overstock", buildStatus(wb, IndexedColors.ORANGE));
		return styles;
	}

	private static CellStyle buildStatus(Workbook wb, IndexedColors colour) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints(BODY_POINTS);
		font.setColor(colour.getIndex());
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		applyBorder(style, BorderStyle.THIN);
		return style;
	}

	private static void applyBorder(CellStyle style, BorderStyle border) {
		style.setBorderTop(border);
		style.setBorderBottom(border);
		style.setBorderLeft(border);
		style.setBorderRight(border);
	}

	/** Number format for a column type, or {@code null} when the type needs none. */
	private static String formatFor(ExcelColumn.Type type) {
		return switch (type) {
			case INTEGER -> FMT_INTEGER;
			case DECIMAL, CURRENCY -> FMT_DECIMAL;
			case DATE -> FMT_DATE;
			case DATETIME -> FMT_DATETIME;
			case TEXT, STATUS -> null;
		};
	}

	/** Normalises a status cell value into the key used by the status style map. */
	private static String statusKey(Object value) {
		return value == null ? "__default" : value.toString().trim().toLowerCase();
	}
}
