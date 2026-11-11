package id.putra.core_erp.service.excel;

import java.util.List;

/**
 * One report's worth of data, ready for {@link ExcelSheetBuilder} to render.
 *
 * <p>Keeping rendering separate from data means a sheet class only has to know how to
 * query its rows; all layout, styling and print setup lives in one place.
 *
 * @param title    report heading, rendered uppercased in the title block
 * @param subtitle metadata line under the title (period, filters), may be blank
 * @param columns  column definitions, in display order
 * @param rows     data rows; each row must have one value per column, in the same order
 */
public record ExcelReport(String title, String subtitle, List<ExcelColumn> columns, List<List<Object>> rows) {

	/** Report with no subtitle line. */
	public static ExcelReport of(String title, List<ExcelColumn> columns, List<List<Object>> rows) {
		return new ExcelReport(title, "", columns, rows);
	}

	/** True when there is no data to render — the builder draws an explicit "no data" row. */
	public boolean isEmpty() {
		return rows == null || rows.isEmpty();
	}
}
