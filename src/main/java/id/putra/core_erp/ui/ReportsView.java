package id.putra.core_erp.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;

import id.putra.core_erp.modules.inbound.model.Receiving;
import id.putra.core_erp.modules.inbound.repository.ReceivingRepository;
import id.putra.core_erp.modules.outbound.model.SalesOrder;
import id.putra.core_erp.modules.outbound.repository.SalesOrderRepository;
import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.product.repository.ProductRepository;
import id.putra.core_erp.modules.transaction.model.Transaction;
import id.putra.core_erp.modules.transaction.repository.TransactionRepository;
import io.quarkus.security.Authenticated;

@Route(value = "reports", layout = MainLayout.class)
@PageTitle("Reports")
@Authenticated
public class ReportsView extends ViewWithDialog {

	private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static final String REPORT_INVENTORY = "Inventory Stock Valuation";
	private static final String REPORT_INBOUND = "Inbound Receipts Summary";
	private static final String REPORT_OUTBOUND = "Outbound Fulfillment Rate";
	private static final String REPORT_AUDIT = "Stock Audit History";

	private final ProductRepository productRepository;
	private final ReceivingRepository receivingRepository;
	private final SalesOrderRepository salesOrderRepository;
	private final TransactionRepository transactionRepository;

	private ComboBox<String> reportType;
	private DatePicker startDate;
	private DatePicker endDate;

	public ReportsView(ProductRepository productRepository, ReceivingRepository receivingRepository,
			SalesOrderRepository salesOrderRepository, TransactionRepository transactionRepository) {
		super();
		this.productRepository = productRepository;
		this.receivingRepository = receivingRepository;
		this.salesOrderRepository = salesOrderRepository;
		this.transactionRepository = transactionRepository;

		addHeader();
		addReportGeneratorSection();
	}

	@Override
	public void addHeader() {
		H2 title = new H2("Reports & Analytics");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Generate and download warehouse inventory and order fulfillment reports.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	private void addReportGeneratorSection() {
		reportType = new ComboBox<>("Report Type");
		reportType.setItems(REPORT_INVENTORY, REPORT_INBOUND, REPORT_OUTBOUND, REPORT_AUDIT);
		reportType.setValue(REPORT_INVENTORY);
		reportType.setRequired(true);

		startDate = new DatePicker("Start Date", LocalDate.now().minusDays(30));
		endDate = new DatePicker("End Date", LocalDate.now());

		FormLayout form = new FormLayout(reportType, startDate, endDate);
		form.setMaxWidth("600px");

		Button generateBtn = new Button("Generate Preview", VaadinIcon.FILE_TEXT.create(), e -> {
			int count = countRecords();
			Notification.show("Preview: " + reportType.getValue() + " — " + count + " record(s) in range", 3000,
					Notification.Position.TOP_END).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		});
		generateBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		DownloadHandler downloadHandler = DownloadHandler.fromInputStream(event -> {
			ByteArrayOutputStream baos = buildExcel();
			ByteArrayInputStream input = new ByteArrayInputStream(baos.toByteArray());
			String fileName = "report_" + reportType.getValue().toLowerCase().replace(" ", "_") + "_"
					+ LocalDate.now() + ".xlsx";
			return new DownloadResponse(input, fileName,
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", baos.size());
		});

		Anchor downloadLink = new Anchor(downloadHandler, "");
		downloadLink.getElement().setAttribute("download", true);
		Button downloadBtn = new Button("Export Excel", VaadinIcon.DOWNLOAD.create());
		downloadBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
		downloadLink.removeAll();
		downloadLink.add(downloadBtn);

		HorizontalLayout actions = new HorizontalLayout(generateBtn, downloadLink);
		actions.setAlignItems(FlexComponent.Alignment.CENTER);

		VerticalLayout container = new VerticalLayout(form, actions);
		container.addClassNames("border", "rounded-l", "p-l", "bg-base");
		container.setMaxWidth("650px");

		add(container);
	}

	/**
	 * Counts records matching the selected report type and date range for the
	 * preview notification.
	 */
	private int countRecords() {
		String type = reportType.getValue();
		LocalDate start = startDate.getValue();
		LocalDate end = endDate.getValue();
		try {
			if (REPORT_INVENTORY.equals(type)) {
				return (int) productRepository.count();
			}
			if (start == null || end == null) {
				return 0;
			}
			LocalDateTime startDt = start.atStartOfDay();
			LocalDateTime endDt = end.atTime(23, 59, 59);
			if (REPORT_INBOUND.equals(type)) {
				return (int) receivingRepository
						.find("receivedDate >= ?1 and receivedDate <= ?2", startDt, endDt).count();
			}
			if (REPORT_OUTBOUND.equals(type)) {
				return (int) salesOrderRepository
						.find("orderDate >= ?1 and orderDate <= ?2", startDt, endDt).count();
			}
			if (REPORT_AUDIT.equals(type)) {
				return (int) transactionRepository.find(
						"txnDate >= ?1 and txnDate <= ?2",
						startDt.atZone(ZoneId.systemDefault()),
						endDt.atZone(ZoneId.systemDefault())).count();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * Builds an XLSX workbook for the selected report type and returns it as a
	 * byte array output stream ready for download.
	 */
	private ByteArrayOutputStream buildExcel() {
		String type = reportType.getValue();
		LocalDate start = startDate.getValue();
		LocalDate end = endDate.getValue();
		try (Workbook workbook = new XSSFWorkbook()) {
			if (REPORT_INVENTORY.equals(type)) {
				buildInventorySheet(workbook);
			} else if (REPORT_INBOUND.equals(type)) {
				buildInboundSheet(workbook, start, end);
			} else if (REPORT_OUTBOUND.equals(type)) {
				buildOutboundSheet(workbook, start, end);
			} else if (REPORT_AUDIT.equals(type)) {
				buildAuditSheet(workbook, start, end);
			} else {
				buildInventorySheet(workbook);
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);
			return baos;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to generate Excel report: " + ex.getMessage(), ex);
		}
	}

	private void buildInventorySheet(Workbook workbook) {
		Sheet sheet = workbook.createSheet("Inventory Valuation");
		CellStyle headerStyle = createHeaderStyle(workbook);

		Row titleRow = sheet.createRow(0);
		titleRow.createCell(0).setCellValue("Inventory Stock Valuation Report");
		titleRow.getCell(0).setCellStyle(createTitleStyle(workbook));
		Row subRow = sheet.createRow(1);
		subRow.createCell(0).setCellValue("Generated: " + LocalDate.now().format(DATE_FMT));

		String[] headers = { "SKU", "Product Name", "Quantity", "Min Stock", "Max Stock", "Stock Status" };
		Row headerRow = sheet.createRow(3);
		for (int i = 0; i < headers.length; i++) {
			var cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

		List<Product> products = productRepository.listAll();
		int rowIdx = 4;
		for (Product p : products) {
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(p.sku != null ? p.sku : "");
			row.createCell(1).setCellValue(p.name != null ? p.name : "");
			row.createCell(2).setCellValue(p.quantity != null ? p.quantity : 0);
			row.createCell(3).setCellValue(p.minStock != null ? p.minStock : 0);
			row.createCell(4).setCellValue(p.maxStock != null ? p.maxStock : 0);
			row.createCell(5).setCellValue(stockStatus(p));
		}
		autoSizeColumns(sheet, headers.length);
	}

	private String stockStatus(Product p) {
		if (p.quantity == null || p.minStock == null) {
			return "UNKNOWN";
		}
		if (p.quantity < p.minStock) {
			return "LOW STOCK";
		}
		if (p.maxStock != null && p.quantity > p.maxStock) {
			return "OVERSTOCK";
		}
		return "OK";
	}

	private void buildInboundSheet(Workbook workbook, LocalDate start, LocalDate end) {
		Sheet sheet = workbook.createSheet("Inbound Receipts");
		CellStyle headerStyle = createHeaderStyle(workbook);

		Row titleRow = sheet.createRow(0);
		titleRow.createCell(0).setCellValue("Inbound Receipts Summary Report");
		titleRow.getCell(0).setCellStyle(createTitleStyle(workbook));
		Row subRow = sheet.createRow(1);
		subRow.createCell(0).setCellValue("Period: " + fmtDate(start) + " to " + fmtDate(end) + " | Generated: "
				+ LocalDate.now().format(DATE_FMT));

		String[] headers = { "Receiving Number", "Received Date", "Status", "PO Number", "Supplier" };
		Row headerRow = sheet.createRow(3);
		for (int i = 0; i < headers.length; i++) {
			var cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

		List<Receiving> receivings = queryReceivings(start, end);
		int rowIdx = 4;
		for (Receiving r : receivings) {
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(r.receivingNumber != null ? r.receivingNumber : "");
			row.createCell(1).setCellValue(r.receivedDate != null ? r.receivedDate.format(DT_FMT) : "");
			row.createCell(2).setCellValue(r.status != null ? r.status.name() : "");
			row.createCell(3).setCellValue(
					r.purchaseOrder != null && r.purchaseOrder.poNumber != null ? r.purchaseOrder.poNumber : "");
			row.createCell(4).setCellValue(r.purchaseOrder != null && r.purchaseOrder.supplier != null
					&& r.purchaseOrder.supplier.name != null ? r.purchaseOrder.supplier.name : "");
		}
		autoSizeColumns(sheet, headers.length);
	}

	private List<Receiving> queryReceivings(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			return receivingRepository.listAll();
		}
		return receivingRepository
				.find("receivedDate >= ?1 and receivedDate <= ?2", start.atStartOfDay(), end.atTime(23, 59, 59))
				.list();
	}

	private void buildOutboundSheet(Workbook workbook, LocalDate start, LocalDate end) {
		Sheet sheet = workbook.createSheet("Outbound Fulfillment");
		CellStyle headerStyle = createHeaderStyle(workbook);

		Row titleRow = sheet.createRow(0);
		titleRow.createCell(0).setCellValue("Outbound Fulfillment Rate Report");
		titleRow.getCell(0).setCellStyle(createTitleStyle(workbook));
		Row subRow = sheet.createRow(1);
		subRow.createCell(0).setCellValue("Period: " + fmtDate(start) + " to " + fmtDate(end) + " | Generated: "
				+ LocalDate.now().format(DATE_FMT));

		String[] headers = { "SO Number", "Order Date", "Status", "Customer", "Line Items" };
		Row headerRow = sheet.createRow(3);
		for (int i = 0; i < headers.length; i++) {
			var cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

		List<SalesOrder> orders = querySalesOrders(start, end);
		int rowIdx = 4;
		for (SalesOrder so : orders) {
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(so.soNumber != null ? so.soNumber : "");
			row.createCell(1).setCellValue(so.orderDate != null ? so.orderDate.format(DT_FMT) : "");
			row.createCell(2).setCellValue(so.status != null ? so.status : "");
			row.createCell(3)
					.setCellValue(so.customer != null && so.customer.name != null ? so.customer.name : "");
			row.createCell(4).setCellValue(so.salesOrderLines != null ? so.salesOrderLines.size() : 0);
		}
		autoSizeColumns(sheet, headers.length);
	}

	private List<SalesOrder> querySalesOrders(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			return salesOrderRepository.listAll();
		}
		return salesOrderRepository
				.find("orderDate >= ?1 and orderDate <= ?2", start.atStartOfDay(), end.atTime(23, 59, 59))
				.list();
	}

	private void buildAuditSheet(Workbook workbook, LocalDate start, LocalDate end) {
		Sheet sheet = workbook.createSheet("Stock Audit History");
		CellStyle headerStyle = createHeaderStyle(workbook);

		Row titleRow = sheet.createRow(0);
		titleRow.createCell(0).setCellValue("Stock Audit / Transaction History Report");
		titleRow.getCell(0).setCellStyle(createTitleStyle(workbook));
		Row subRow = sheet.createRow(1);
		subRow.createCell(0).setCellValue("Period: " + fmtDate(start) + " to " + fmtDate(end) + " | Generated: "
				+ LocalDate.now().format(DATE_FMT));

		String[] headers = { "Txn Number", "Txn Type", "Txn Date", "Status", "Total Amount", "Quantity",
				"Ref Number" };
		Row headerRow = sheet.createRow(3);
		for (int i = 0; i < headers.length; i++) {
			var cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

		List<Transaction> txns = queryTransactions(start, end);
		int rowIdx = 4;
		for (Transaction t : txns) {
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(t.txnNumber != null ? t.txnNumber : "");
			row.createCell(1).setCellValue(t.txnType != null ? t.txnType : "");
			row.createCell(2).setCellValue(t.txnDate != null ? t.txnDate.format(DT_FMT) : "");
			row.createCell(3).setCellValue(t.status != null ? t.status : "");
			row.createCell(4).setCellValue(t.totalAmount != null ? t.totalAmount.doubleValue() : 0);
			row.createCell(5).setCellValue(t.quantity != null ? t.quantity : 0);
			row.createCell(6).setCellValue(t.refNumber != null ? t.refNumber : "");
		}
		autoSizeColumns(sheet, headers.length);
	}

	private List<Transaction> queryTransactions(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			return transactionRepository.listAll();
		}
		return transactionRepository.find("txnDate >= ?1 and txnDate <= ?2",
				start.atStartOfDay().atZone(ZoneId.systemDefault()),
				end.atTime(23, 59, 59).atZone(ZoneId.systemDefault())).list();
	}

	private CellStyle createHeaderStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;
	}

	private CellStyle createTitleStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		return style;
	}

	private void autoSizeColumns(Sheet sheet, int columnCount) {
		for (int i = 0; i < columnCount; i++) {
			sheet.autoSizeColumn(i);
		}
	}

	private String fmtDate(LocalDate date) {
		return date != null ? date.format(DATE_FMT) : "N/A";
	}

	@Override
	public void addToolbar() {
	}

	@Override
	public void refreshData() {
	}

	@Override
	public void configureGrid() {
	}

	@Override
	protected void buildAndShowDialog(Object entity) throws Exception {
	}

	@Override
	public void confirmDelete(Object entity) {
	}
}
