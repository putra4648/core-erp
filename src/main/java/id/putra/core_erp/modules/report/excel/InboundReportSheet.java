package id.putra.core_erp.modules.report.excel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import id.putra.core_erp.modules.inbound.model.Receiving;
import id.putra.core_erp.modules.inbound.repository.ReceivingRepository;
import id.putra.core_erp.service.excel.ExcelColumn;
import id.putra.core_erp.service.excel.ExcelColumn.Type;
import id.putra.core_erp.service.excel.ExcelReport;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Inbound receipts summary — goods received from suppliers over a period.
 *
 * <p>Dates are written as real Excel dates rather than pre-formatted strings so the
 * recipient can sort, filter and group by them.
 */
@ApplicationScoped
public class InboundReportSheet {

	private static final List<ExcelColumn> COLUMNS = List.of(
			ExcelColumn.of("No.", 6, Type.INTEGER, HorizontalAlignment.CENTER),
			ExcelColumn.of("Receiving Number", 22, Type.TEXT),
			ExcelColumn.of("Received Date", 18, Type.DATETIME),
			ExcelColumn.of("Status", 14, Type.STATUS),
			ExcelColumn.of("PO Number", 22, Type.TEXT),
			ExcelColumn.of("Supplier", 32, Type.TEXT));

	private final ReceivingRepository receivingRepository;

	public InboundReportSheet(ReceivingRepository receivingRepository) {
		this.receivingRepository = receivingRepository;
	}

	public ExcelReport build(LocalDate start, LocalDate end) {
		List<Receiving> receivings = query(start, end);
		List<List<Object>> rows = new ArrayList<>(receivings.size());
		int index = 1;
		for (Receiving r : receivings) {
			rows.add(java.util.Arrays.asList(
					index++,
					r.receivingNumber,
					r.receivedDate,
					r.status != null ? r.status.name() : null,
					r.purchaseOrder != null ? r.purchaseOrder.poNumber : null,
					r.purchaseOrder != null && r.purchaseOrder.supplier != null
							? r.purchaseOrder.supplier.name
							: null));
		}
		return new ExcelReport("Inbound Receipts Summary Report", ReportPeriod.describe(start, end), COLUMNS, rows);
	}

	private List<Receiving> query(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			return receivingRepository.listAll();
		}
		return receivingRepository
				.find("receivedDate >= ?1 and receivedDate <= ?2", start.atStartOfDay(), end.atTime(23, 59, 59))
				.list();
	}

	/** Number of records the report would contain, without building the whole workbook. */
	public long count(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			return receivingRepository.count();
		}
		return query(start, end).size();
	}
}
