package id.putra.core_erp.modules.report.excel;

import java.time.LocalDate;

/**
 * Shared helpers for report metadata lines.
 */
final class ReportPeriod {

	private ReportPeriod() {
	}

	/** Human-readable period line for the report subtitle. */
	static String describe(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			return "Period: all records  \u00b7  Generated: " + LocalDate.now();
		}
		return "Period: " + start + " to " + end + "  \u00b7  Generated: " + LocalDate.now();
	}
}
