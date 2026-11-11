package id.putra.core_erp.modules.report.excel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import id.putra.core_erp.modules.outbound.model.SalesOrder;
import id.putra.core_erp.modules.outbound.repository.SalesOrderRepository;
import id.putra.core_erp.service.excel.ExcelColumn;
import id.putra.core_erp.service.excel.ExcelColumn.Type;
import id.putra.core_erp.service.excel.ExcelReport;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Outbound fulfillment report — sales orders and their line item counts over a period.
 */
@ApplicationScoped
public class OutboundReportSheet {

	private static final List<ExcelColumn> COLUMNS = List.of(
			ExcelColumn.of("No.", 6, Type.INTEGER, HorizontalAlignment.CENTER),
			ExcelColumn.of("SO Number", 22, Type.TEXT),
			ExcelColumn.of("Order Date", 18, Type.DATETIME),
			ExcelColumn.of("Status", 14, Type.STATUS),
			ExcelColumn.of("Customer", 32, Type.TEXT),
			ExcelColumn.of("Line Items", 12, Type.INTEGER));

	private final SalesOrderRepository salesOrderRepository;

	public OutboundReportSheet(SalesOrderRepository salesOrderRepository) {
		this.salesOrderRepository = salesOrderRepository;
	}

	public ExcelReport build(LocalDate start, LocalDate end) {
		List<SalesOrder> orders = query(start, end);
		List<List<Object>> rows = new ArrayList<>(orders.size());
		int index = 1;
		for (SalesOrder so : orders) {
			rows.add(java.util.Arrays.asList(
					index++,
					so.soNumber,
					so.orderDate,
					so.status,
					so.customer != null ? so.customer.name : null,
					so.salesOrderLines != null ? so.salesOrderLines.size() : null));
		}
		return new ExcelReport("Outbound Fulfillment Rate Report", ReportPeriod.describe(start, end), COLUMNS, rows);
	}

	private List<SalesOrder> query(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			return salesOrderRepository.listAll();
		}
		return salesOrderRepository
				.find("orderDate >= ?1 and orderDate <= ?2", start.atStartOfDay(), end.atTime(23, 59, 59))
				.list();
	}

	/** Number of records the report would contain, without building the whole workbook. */
	public long count(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			return salesOrderRepository.count();
		}
		return query(start, end).size();
	}
}
