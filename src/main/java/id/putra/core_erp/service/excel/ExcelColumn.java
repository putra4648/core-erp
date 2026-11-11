package id.putra.core_erp.service.excel;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * Declarative description of one column in a formal Excel report.
 *
 * <p>Column width is explicit rather than computed via {@code Sheet#autoSizeColumn}.
 * Auto-sizing depends on the fonts installed in the JVM — on a headless server without
 * Arial the widths come out arbitrary, and on wide text columns it produces unusably
 * large sheets. Explicit widths are deterministic across environments.
 *
 * @param header display label in the header row
 * @param width  column width in Excel character units (1 unit ≈ width of one '0')
 * @param type   value type, drives both the cell writer and the number format
 * @param align  horizontal alignment override; {@code null} uses the type's default
 */
public record ExcelColumn(String header, int width, Type type, HorizontalAlignment align) {

	/** Value kinds a report column can hold. Each maps to a number format and default alignment. */
	public enum Type {
		/** Free text. Left aligned, no format. */
		TEXT,
		/** Whole number. Right aligned, {@code #,##0}. */
		INTEGER,
		/** Fractional number. Right aligned, {@code #,##0.00}. */
		DECIMAL,
		/** Money amount. Right aligned, {@code #,##0.00}. */
		CURRENCY,
		/** Calendar date. Centred, {@code yyyy-mm-dd}, stored as a real Excel date. */
		DATE,
		/** Date with time. Centred, {@code yyyy-mm-dd hh:mm}, stored as a real Excel date. */
		DATETIME,
		/** Short state label. Centred, text coloured by value. */
		STATUS
	}

	/** Column with the type's default alignment; use when no override is needed. */
	public static ExcelColumn of(String header, int width, Type type) {
		return new ExcelColumn(header, width, type, null);
	}

	/** Column with an explicit alignment, overriding the type default. */
	public static ExcelColumn of(String header, int width, Type type, HorizontalAlignment align) {
		return new ExcelColumn(header, width, type, align);
	}

	/** Effective alignment: the explicit override when set, otherwise the type default. */
	public HorizontalAlignment effectiveAlignment() {
		if (align != null) {
			return align;
		}
		return switch (type) {
			case TEXT -> HorizontalAlignment.LEFT;
			case INTEGER, DECIMAL, CURRENCY -> HorizontalAlignment.RIGHT;
			case DATE, DATETIME, STATUS -> HorizontalAlignment.CENTER;
		};
	}

	/** True when this column holds numbers and therefore participates in the total row. */
	public boolean isNumeric() {
		return type == Type.INTEGER || type == Type.DECIMAL || type == Type.CURRENCY;
	}
}
