package id.putra.core_erp.modules.report.excel;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Workbook;

import id.putra.core_erp.service.excel.ExcelReport;
import id.putra.core_erp.service.excel.ExcelSheetBuilder;
import id.putra.core_erp.service.excel.ExcelStyles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * The reports offered on the Reports &amp; Analytics screen.
 *
 * <p>
 * Each constant owns the label shown in the UI and the sheet class that renders
 * it, so
 * adding a report means adding one constant and one class — no switch statement
 * to extend
 * in the view, and no chance of the dropdown drifting out of sync with the
 * builders.
 */
public enum ReportType {

	INVENTORY_STOCK_STATUS("Inventory Stock Status", "Inventory Stock Status"),
	INBOUND_RECEIPTS("Inbound Receipts Summary", "Inbound Receipts"),
	OUTBOUND_FULFILLMENT("Outbound Fulfillment Rate", "Outbound Fulfillment"),
	STOCK_AUDIT("Stock Audit History", "Stock Audit History");

	private final String label;
	private final String sheetName;

	ReportType(String label, String sheetName) {
		this.label = label;
		this.sheetName = sheetName;
	}

	/** Text shown in the report type dropdown. */
	public String label() {
		return label;
	}

	/** Worksheet tab name. Excel caps tab names at 31 characters. */
	public String sheetName() {
		return sheetName;
	}

	/**
	 * File name stem, e.g. {@code inventory_stock_status}. Uppercase labels and
	 * punctuation
	 * are normalised away so the name is safe on every filesystem.
	 */
	public String fileStem() {
		return name().toLowerCase().replace('_', '-');
	}

	/**
	 * True when the report covers a date range and the pickers should be enabled.
	 */
	public boolean isDateRanged() {
		return this != INVENTORY_STOCK_STATUS;
	}

	/** All labels, in declaration order — used to populate the dropdown. */
	public static String[] labels() {
		return Arrays.stream(values()).map(ReportType::label).toArray(String[]::new);
	}

	/** Resolves a dropdown label back to its constant. */
	public static Optional<ReportType> fromLabel(String label) {
		return Arrays.stream(values()).filter(t -> t.label.equals(label)).findFirst();
	}

	/**
	 * Renders this report into {@code workbook}.
	 *
	 * <p>
	 * Kept here rather than on the sheet classes so the dispatch stays in one place
	 * and
	 * the sheet classes can remain plain beans with constructor-injected
	 * repositories.
	 *
	 * @param workbook the target workbook
	 * @param start    range start, may be {@code null} for non-ranged reports
	 * @param end      range end, may be {@code null}
	 * @param sheets   holder providing the sheet beans
	 */
	public void render(Workbook workbook, LocalDate start, LocalDate end, ReportSheets sheets) {
		ExcelStyles styles = new ExcelStyles(workbook);
		ExcelSheetBuilder builder = new ExcelSheetBuilder(workbook, styles);
		ExcelReport report = switch (this) {
			case INVENTORY_STOCK_STATUS -> sheets.inventory().build();
			case INBOUND_RECEIPTS -> sheets.inbound().build(start, end);
			case OUTBOUND_FULFILLMENT -> sheets.outbound().build(start, end);
			case STOCK_AUDIT -> sheets.audit().build(start, end);
		};
		builder.build(sheetName, report, "CORE ERP");
	}

	/**
	 * Aggregates the sheet beans so {@link #render} takes one argument instead of
	 * four.
	 */
	@ApplicationScoped
	public static final class ReportSheets {
		@Inject
		InventoryReportSheet inventory;
		@Inject
		InboundReportSheet inbound;
		@Inject
		OutboundReportSheet outbound;
		@Inject
		AuditReportSheet audit;

		public InventoryReportSheet inventory() {
			return inventory;
		}

		public InboundReportSheet inbound() {
			return inbound;
		}

		public OutboundReportSheet outbound() {
			return outbound;
		}

		public AuditReportSheet audit() {
			return audit;
		}
	}
}
