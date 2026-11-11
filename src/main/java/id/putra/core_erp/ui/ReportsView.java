package id.putra.core_erp.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

import id.putra.core_erp.modules.report.excel.ReportType;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;

/**
 * Reports &amp; Analytics screen.
 *
 * <p>This view owns only the selection UI and the download plumbing. Everything about how a
 * report looks lives in {@code service.excel}, and everything about what a report contains
 * lives in {@code modules.report.excel} — so adding a report is a new sheet class plus one
 * {@link ReportType} constant, with no change here.
 */
@Route(value = "reports", layout = MainLayout.class)
@PageTitle("Reports")
@Authenticated
public class ReportsView extends ViewWithDialog {

	private static final String XLSX_MIME = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@Inject
	ReportType.ReportSheets sheets;

	private ComboBox<ReportType> reportType;
	private DatePicker startDate;
	private DatePicker endDate;

	public ReportsView() {
		super();
		addHeader();
		addReportGeneratorSection();
	}

	@Override
	public void addHeader() {
		H2 title = new H2("Reports & Analytics");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph(
				"Generate and download warehouse inventory and order fulfillment reports as styled Excel workbooks.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	private void addReportGeneratorSection() {
		reportType = new ComboBox<>("Report Type");
		reportType.setItems(ReportType.values());
		reportType.setItemLabelGenerator(ReportType::label);
		reportType.setValue(ReportType.INVENTORY_STOCK_STATUS);
		reportType.setRequired(true);

		startDate = new DatePicker("Start Date", LocalDate.now().minusDays(30));
		endDate = new DatePicker("End Date", LocalDate.now());

		// The inventory report is a current-state snapshot, not a ranged query. Leaving the
		// pickers enabled would imply a filter that the report does not apply.
		applyDateRangeState(ReportType.INVENTORY_STOCK_STATUS);
		reportType.addValueChangeListener(e -> {
			ReportType selected = e.getValue();
			if (selected != null) {
				applyDateRangeState(selected);
			}
		});

		FormLayout form = new FormLayout(reportType, startDate, endDate);
		form.setMaxWidth("600px");

		Button generateBtn = new Button("Generate Preview", VaadinIcon.FILE_TEXT.create(), e -> onPreview());
		generateBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Anchor downloadLink = new Anchor(buildDownloadHandler(), "");
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

	private void applyDateRangeState(ReportType type) {
		boolean ranged = type.isDateRanged();
		startDate.setEnabled(ranged);
		endDate.setEnabled(ranged);
		startDate.setHelperText(ranged ? null : "Not used by this report");
		endDate.setHelperText(ranged ? null : "Not used by this report");
	}

	/** Counts the rows the selected report would contain and reports it to the user. */
	private void onPreview() {
		ReportType type = reportType.getValue();
		if (type == null) {
			showError("Pilih jenis laporan terlebih dahulu.");
			return;
		}
		LocalDate start = startDate.getValue();
		LocalDate end = endDate.getValue();
		try {
			long count = countFor(type, start, end);
			Notification.show("Preview: " + type.label() + " \u2014 " + count + " record(s)", 3000,
					Notification.Position.TOP_END).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		} catch (Exception ex) {
			showError("Gagal menghitung data: " + rootMessage(ex));
		}
	}

	private long countFor(ReportType type, LocalDate start, LocalDate end) {
		return switch (type) {
			case INVENTORY_STOCK_STATUS -> sheets.inventory().build().rows().size();
			case INBOUND_RECEIPTS -> sheets.inbound().count(start, end);
			case OUTBOUND_FULFILLMENT -> sheets.outbound().count(start, end);
			case STOCK_AUDIT -> sheets.audit().count(start, end);
		};
	}

	/**
	 * Builds the download handler.
	 *
	 * <p>The supplier runs on a background thread, so a failure here cannot surface as an
	 * exception on the UI — the user would just get nothing. It is caught and rethrown as a
	 * {@link IllegalStateException} with a readable message, and the view shows the error
	 * notification before the download is attempted.
	 */
	private DownloadHandler buildDownloadHandler() {
		return DownloadHandler.fromInputStream(event -> {
			ReportType type = reportType.getValue();
			if (type == null) {
				throw new IllegalStateException("Pilih jenis laporan terlebih dahulu.");
			}
			LocalDate start = startDate.getValue();
			LocalDate end = endDate.getValue();
			// toByteArray() copies the whole workbook; call it once and reuse the array.
			byte[] bytes = buildExcel(type, start, end);
			String fileName = LocalDate.now() + "_" + type.fileStem() + ".xlsx";
			return new DownloadResponse(new ByteArrayInputStream(bytes), fileName, XLSX_MIME, bytes.length);
		});
	}

	/**
	 * Renders the selected report into an XLSX byte array.
	 *
	 * <p>{@code XSSFWorkbook} holds the whole document in memory. That is fine for the row
	 * counts this ERP produces; if a report ever exceeds roughly 50,000 rows, switch to
	 * {@code SXSSFWorkbook} — at the cost of losing the merged title block.
	 */
	private byte[] buildExcel(ReportType type, LocalDate start, LocalDate end) {
		try (Workbook workbook = new XSSFWorkbook()) {
			type.render(workbook, start, end, sheets);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);
			return baos.toByteArray();
		} catch (Exception ex) {
			throw new IllegalStateException("Gagal membuat laporan Excel: " + rootMessage(ex), ex);
		}
	}

	private void showError(String message) {
		Notification.show(message, 5000, Notification.Position.TOP_END)
				.addThemeVariants(NotificationVariant.LUMO_ERROR);
	}

	private static String rootMessage(Throwable ex) {
		Throwable cause = ex;
		while (cause.getCause() != null && cause.getCause() != cause) {
			cause = cause.getCause();
		}
		return cause.getMessage() != null ? cause.getMessage() : cause.getClass().getSimpleName();
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
