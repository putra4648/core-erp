package id.putra.core_erp.modules.procurement.views;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.modules.procurement.model.Quotation;
import id.putra.core_erp.modules.procurement.service.QuotationService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.security.Authenticated;

@Route(value = "quotation", layout = MainLayout.class)
@PageTitle("Quotation | PR System")
@Authenticated
public class QuotationView extends ViewWithDialog {

	private final QuotationService quotationService;

	private final TextField searchField = new TextField();
	private final Grid<Quotation> grid = new Grid<>();
	private final List<Quotation> rfqList = new ArrayList<>();

	// dialog fields
	private final TextField rfqNumberField = new TextField("RFQ Number");
	private final DatePicker dueDateField = new DatePicker("Due Date");
	private final TextArea noteField = new TextArea("Notes");
	private Quotation editing;

	public QuotationView(QuotationService rfqService) {
		super();
		this.quotationService = rfqService;

		addHeader();
		addToolbar();
		configureGrid();

	}

	@Override
	public void addHeader() {
		H2 header = new H2("Quotation");
		header.addClassNames(LumoUtility.FontSize.XLARGE);
		Paragraph desc = new Paragraph(
				"Manage Request for Quotations (RFQ) and generate Purchase Orders from approved bids.");
		desc.addClassNames("text-secondary", "m-0");
		add(header, desc);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search RFQ by ID or Note...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.setWidth("280px");
		searchField.addValueChangeListener(e -> refreshData());

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		Button addBtn = new Button("Add Request For Quotation", VaadinIcon.PLUS.create(),
				e -> openDialog(new Quotation()));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
		toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

		add(toolbar);
	}

	@Override
	public void configureGrid() {

		grid.setSizeFull();
		grid.addColumn(r -> r.rfqNumber != null ? r.rfqNumber : (r.id != null ? "RFQ-" + r.id : "-"))
				.setHeader("RFQ Number").setAutoWidth(true);
		grid.addColumn(r -> r.dueDate != null ? r.dueDate.toString() : "-").setHeader("Due Date")
				.setAutoWidth(true);
		grid.addColumn(r -> r.selectedSupplier != null ? r.selectedSupplier.name : "-")
				.setHeader("Winning Supplier").setFlexGrow(1);
		grid.addColumn(r -> r.suppliers != null ? r.suppliers.size() + " suppliers" : "0 suppliers")
				.setHeader("Candidates").setAutoWidth(true);
		grid.addColumn(r -> r.purchaseOrder != null ? r.purchaseOrder.poNumber : "-").setHeader("Purchase Order")
				.setAutoWidth(true);
		grid.addColumn(r -> r.note != null ? r.note : "-").setHeader("Notes").setFlexGrow(2);

		grid.addComponentColumn(rfq -> {
			String status = rfq.status != null ? rfq.status.name() : "SUBMIT";
			Span badge = new Span(status);
			if ("APPROVED".equals(status) || "COMPLETED".equals(status)) {
				badge.getElement().getThemeList().add("badge success");
			} else if ("REJECTED".equals(status)) {
				badge.getElement().getThemeList().add("badge error");
			} else {
				badge.getElement().getThemeList().add("badge");
			}
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		grid.addComponentColumn(rfq -> {
			Button genPo = new Button(VaadinIcon.FILE_TEXT.create(), e -> generatePo(rfq));
			genPo.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
			if (rfq.purchaseOrder != null) {
				genPo.setText(rfq.purchaseOrder.poNumber);
				genPo.setEnabled(false);
			} else if (ApprovalStatus.APPROVED == rfq.status) {
				genPo.setText("Generate PO");
			} else {
				genPo.setText("Generate PO");
				genPo.setEnabled(false);
			}
			return genPo;
		}).setHeader("Action").setAutoWidth(true);

		grid.addItemClickListener(e -> openDialog(e.getItem()));

		add(grid);

		refreshData();

	}

	/**
	 * Generates a Purchase Order from an approved quotation (the
	 * {@code Quotation -> Purchase Order} P2P step) and refreshes the grid.
	 */
	private void generatePo(Quotation rfq) {
		try {
			var po = quotationService.generatePurchaseOrder(rfq.id);
			Notification.show("Purchase Order generated: " + po.poNumber, 3000, Notification.Position.TOP_END)
					.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			refreshData();
		} catch (Exception ex) {
			Notification.show("Failed to generate PO: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	@Override
	public void refreshData() {
		try {
			Quotation params = new Quotation();
			params.rfqNumber = searchField.getValue();

			List<Quotation> res = quotationService.findAll(params);
			rfqList.clear();
			if (res != null) {
				rfqList.addAll(res);
			}
			grid.setItems(rfqList);
		} catch (Exception ex) {
			Notification.show("Failed to load RFQs: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	@Override
	protected void buildAndShowDialog(@NonNull Object entity) throws Exception {
		editing = entity != null ? (Quotation) entity : new Quotation();
		rfqNumberField.clear();
		dueDateField.clear();
		noteField.clear();
		if (editing.id != null) {
			rfqNumberField.setValue(editing.rfqNumber != null ? editing.rfqNumber : "");
			dueDateField.setValue(editing.dueDate);
			noteField.setValue(editing.note != null ? editing.note : "");
		}

		Dialog dialog = new Dialog(editing.id == null ? "New Request For Quotation" : "Edit Quotation");
		dialog.setWidth("520px");

		FormLayout form = new FormLayout(rfqNumberField, dueDateField, noteField);
		form.setColspan(noteField, 2);
		noteField.setWidthFull();
		dialog.add(form);

		Button saveBtn = new Button("Save", e -> {
			try {
				editing.rfqNumber = rfqNumberField.getValue();
				editing.dueDate = dueDateField.getValue();
				editing.note = noteField.getValue();
				if (editing.id != null) {
					quotationService.update(editing);
					Notification.show("Quotation updated successfully", 3000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				} else {
					quotationService.save(editing);
					Notification.show("Quotation created successfully", 3000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				}
				dialog.close();
				refreshData();
			} catch (Exception ex) {
				Notification.show("Failed to save quotation: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		dialog.getFooter().add(new Button("Cancel", ev -> dialog.close()), saveBtn);
		dialog.open();
	}

	@Override
	public void confirmDelete(Object entity) {
		Quotation rfq = (Quotation) entity;
		Dialog confirmDialog = new Dialog("Delete Quotation");
		confirmDialog.add(new Paragraph("Are you sure you want to delete RFQ '"
				+ (rfq.rfqNumber != null ? rfq.rfqNumber : "RFQ-" + rfq.id) + "'?"));

		Button deleteBtn = new Button("Delete", e -> {
			try {
				quotationService.delete(rfq.id);
				Notification.show("Quotation deleted successfully", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				confirmDialog.close();
				refreshData();
			} catch (Exception ex) {
				Notification.show("Failed to delete quotation: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

		confirmDialog.getFooter().add(new Button("Cancel", ev -> confirmDialog.close()), deleteBtn);
		confirmDialog.open();
	}
}
