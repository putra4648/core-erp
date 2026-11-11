package id.putra.core_erp.modules.report.excel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import id.putra.core_erp.modules.transaction.model.Transaction;
import id.putra.core_erp.modules.transaction.repository.TransactionRepository;
import id.putra.core_erp.service.excel.ExcelColumn;
import id.putra.core_erp.service.excel.ExcelColumn.Type;
import id.putra.core_erp.service.excel.ExcelReport;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Stock audit history — the transaction ledger over a period.
 *
 * <p>{@code txnDate} is a {@code ZonedDateTime}; it is converted to local time before being
 * written so the displayed timestamp matches what the operator saw in the UI.
 */
@ApplicationScoped
public class AuditReportSheet {

	private static final List<ExcelColumn> COLUMNS = List.of(
			ExcelColumn.of("No.", 6, Type.INTEGER, HorizontalAlignment.CENTER),
			ExcelColumn.of("Txn Number", 20, Type.TEXT),
			ExcelColumn.of("Txn Type", 16, Type.TEXT),
			ExcelColumn.of("Txn Date", 18, Type.DATETIME),
			ExcelColumn.of("Status", 14, Type.STATUS),
			ExcelColumn.of("Total Amount", 18, Type.CURRENCY),
			ExcelColumn.of("Quantity", 12, Type.INTEGER),
			ExcelColumn.of("Ref Number", 20, Type.TEXT));

	private final TransactionRepository transactionRepository;

	public AuditReportSheet(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public ExcelReport build(LocalDate start, LocalDate end) {
		List<Transaction> txns = query(start, end);
		List<List<Object>> rows = new ArrayList<>(txns.size());
		int index = 1;
		for (Transaction t : txns) {
			rows.add(java.util.Arrays.asList(
					index++,
					t.txnNumber,
					t.txnType,
					t.txnDate != null ? t.txnDate.withZoneSameInstant(ZoneId.systemDefault()) : null,
					t.status,
					t.totalAmount,
					t.quantity,
					t.refNumber));
		}
		return new ExcelReport("Stock Audit / Transaction History Report", ReportPeriod.describe(start, end),
				COLUMNS, rows);
	}

	private List<Transaction> query(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			return transactionRepository.listAll();
		}
		return transactionRepository.find("txnDate >= ?1 and txnDate <= ?2",
				start.atStartOfDay().atZone(ZoneId.systemDefault()),
				end.atTime(23, 59, 59).atZone(ZoneId.systemDefault())).list();
	}

	/** Number of records the report would contain, without building the whole workbook. */
	public long count(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			return transactionRepository.count();
		}
		return query(start, end).size();
	}
}
