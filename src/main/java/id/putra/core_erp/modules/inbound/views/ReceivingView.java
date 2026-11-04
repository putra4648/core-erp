package id.putra.core_erp.modules.inbound.views;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.enums.OrderStatus;
import id.putra.core_erp.modules.inbound.model.Receiving;
import id.putra.core_erp.modules.inbound.model.ReceivingLine;
import id.putra.core_erp.modules.inbound.service.InspectionService;
import id.putra.core_erp.modules.inbound.service.PutawayService;
import id.putra.core_erp.modules.inbound.service.ReceivingOrderService;
import id.putra.core_erp.modules.warehouse.model.Warehouse;
import id.putra.core_erp.modules.warehouse.service.WarehouseService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "receiving", layout = MainLayout.class)
@PageTitle("Receiving")
@Authenticated
public class ReceivingView extends ViewWithDialog {

	private final ReceivingOrderService receivingOrderService;
	private final PutawayService putawayService;
	private final InspectionService inspectionService;
	private final WarehouseService warehouseService;
	private final Grid<Receiving> grid = new Grid<>();
	private final TextField searchField = new TextField();

	public ReceivingView(ReceivingOrderService receivingOrderService, PutawayService putawayService,
			InspectionService inspectionService, WarehouseService warehouseService) {
		super();
		this.receivingOrderService = receivingOrderService;
		this.putawayService = putawayService;
		this.inspectionService = inspectionService;
		this.warehouseService = warehouseService;

		addHeader();
		addToolbar();
		configureGrid();

	}

	@Override
	public void addHeader() {
		H2 title = new H2("Goods Receiving");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Process inbound goods receipts and assign storage locations.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search receiving number...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.addValueChangeListener(e -> refreshData());

		Button addButton = new Button("New Goods Receipt", VaadinIcon.PLUS.create(),
				e -> openDialog(new Receiving()));
		addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshButton = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton, refreshButton);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, searchField);
		add(toolbar);
	}

	@Override
	public void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(r -> r.receivingNumber != null ? r.receivingNumber : "-")
				.setHeader("Receiving #").setAutoWidth(true);
		grid.addColumn(r -> r.purchaseOrder != null ? r.purchaseOrder.poNumber : "-")
				.setHeader("PO Number").setFlexGrow(1);
		grid.addColumn(r -> r.purchaseOrder != null && r.purchaseOrder.supplier != null
				? r.purchaseOrder.supplier.name
				: "-")
				.setHeader("Supplier").setFlexGrow(1);
		grid.addColumn(r -> r.receivedDate != null ? r.receivedDate.toLocalDate().toString() : "-")
				.setHeader("Received Date").setAutoWidth(true);
		grid.addColumn(r -> r.receivingLines != null ? r.receivingLines.size() + " lines" : "0 lines")
				.setHeader("Lines").setAutoWidth(true);

		grid.addComponentColumn(receiving -> {
			String status = receiving.status != null ? receiving.status.name() : OrderStatus.PENDING.name();
			Span badge = new Span(status);
			if (OrderStatus.COMPLETED.name().equals(status)) {
				badge.getElement().getThemeList().add("badge success");
			} else {
				badge.getElement().getThemeList().add("badge");
			}
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		grid.addComponentColumn(receiving -> {
			Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDialog(receiving));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			Button completeBtn = new Button("Complete", VaadinIcon.CHECK.create(), e -> completeReceiving(receiving));
			completeBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
			if (receiving.status == OrderStatus.COMPLETED) {
				completeBtn.setEnabled(false);
			}

			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(receiving));
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

			return new HorizontalLayout(editBtn, completeBtn, deleteBtn);
		}).setHeader("Actions").setAutoWidth(true);

		add(grid);

		refreshData();
	}

	private void completeReceiving(Receiving receiving) {
		try {
			receivingOrderService.completeReceiving(receiving.id);
			Notification.show("Receiving completed and PO quantities updated", 3000, Notification.Position.TOP_END)
					.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			refreshData();
		} catch (Exception ex) {
			Notification.show("Failed to complete receiving: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	@Override
	public void refreshData() {
		var entity = new Receiving();
		grid.setItems(receivingOrderService.findAll(entity, Page.ofSize(100)));
	}

	@Override
	protected void buildAndShowDialog(@NonNull Object entity) throws Exception {
		Receiving receiving = (Receiving) entity;
		boolean isEdit = receiving.id != null;
		Dialog dialog = new Dialog(isEdit ? "Edit Goods Receipt" : "New Goods Receipt");
		dialog.setWidth("900px");

		TextField receivingNumberField = new TextField("Receiving #");
		receivingNumberField.setReadOnly(true);
		receivingNumberField.setValue(receiving.receivingNumber != null ? receiving.receivingNumber : "(auto)");

		TextField poField = new TextField("Purchase Order");
		poField.setReadOnly(true);
		poField.setValue(receiving.purchaseOrder != null && receiving.purchaseOrder.poNumber != null
				? receiving.purchaseOrder.poNumber
				: "-");

		HorizontalLayout header = new HorizontalLayout(receivingNumberField, poField);
		header.setWidthFull();

		VerticalLayout content = new VerticalLayout(header);

		Grid<ReceivingLine> linesGrid = new Grid<>();
		List<ReceivingLine> lines = new ArrayList<>();
		if (isEdit && receiving.receivingLines != null) {
			lines.addAll(receiving.receivingLines);
		}

		linesGrid.addColumn(l -> l.product != null ? l.product.sku : "-").setHeader("SKU").setAutoWidth(true);
		linesGrid.addColumn(l -> l.product != null ? l.product.name : "-").setHeader("Product").setFlexGrow(1);
		linesGrid.addColumn(l -> l.qty != null ? l.qty.toPlainString() : "-").setHeader("Ordered").setAutoWidth(true);

		linesGrid.addComponentColumn(line -> {
			IntegerField qtyField = new IntegerField();
			qtyField.setValue(line.qtyReceived != null ? line.qtyReceived.intValue() : 0);
			qtyField.addValueChangeListener(e -> line.qtyReceived = BigDecimal.valueOf(e.getValue()));
			return qtyField;
		}).setHeader("Received").setAutoWidth(true);

		linesGrid.addComponentColumn(line -> {
			TextField lotField = new TextField();
			lotField.setValue(line.lotBatch != null ? line.lotBatch : "");
			lotField.addValueChangeListener(e -> line.lotBatch = e.getValue());
			return lotField;
		}).setHeader("Lot/Batch").setAutoWidth(true);

		linesGrid.addComponentColumn(line -> {
			DatePicker expiryField = new DatePicker();
			if (line.expiryDate != null) {
				expiryField.setValue(line.expiryDate.toLocalDate());
			}
			expiryField.addValueChangeListener(
					e -> line.expiryDate = e.getValue() != null ? e.getValue().atStartOfDay() : null);
			return expiryField;
		}).setHeader("Expiry").setAutoWidth(true);

		linesGrid.addComponentColumn(line -> {
			String s = line.status != null ? line.status : "PENDING_QC";
			Span badge = new Span(s);
			if ("PASSED".equals(s) || "PUTAWAY".equals(s)) {
				badge.getElement().getThemeList().add("badge success");
			} else if ("REJECTED".equals(s)) {
				badge.getElement().getThemeList().add("badge error");
			} else {
				badge.getElement().getThemeList().add("badge");
			}
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		linesGrid.addComponentColumn(line -> {
			Button inspectBtn = new Button(VaadinIcon.EYE.create(), e -> openInspectDialog(line, linesGrid));
			inspectBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);

			Button putawayBtn = new Button(VaadinIcon.ARROW_FORWARD.create(), e -> openPutawayDialog(line, linesGrid));
			putawayBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
			if (!"PASSED".equals(line.status)) {
				putawayBtn.setEnabled(false);
			}
			return new HorizontalLayout(inspectBtn, putawayBtn);
		}).setHeader("Actions").setAutoWidth(true);

		linesGrid.setItems(lines);
		linesGrid.setHeight("300px");

		if (!lines.isEmpty()) {
			content.add(linesGrid);
		} else if (isEdit) {
			content.add(new Paragraph("This receiving has no line items."));
		}

		dialog.add(content);

		Button saveBtn = new Button("Save", e -> {
			try {
				if (isEdit) {
					receivingOrderService.update(receiving);
					for (ReceivingLine line : lines) {
						receivingOrderService.updateLine(line);
					}
				} else {
					receivingOrderService.save(receiving);
				}
				Notification.show("Receiving record saved successfully", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				dialog.close();
				refreshData();
			} catch (Exception ex) {
				Notification.show("Failed to save receiving: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		dialog.getFooter().add(new Button("Cancel", e -> dialog.close()), saveBtn);
		dialog.open();
	}

	@Override
	public void confirmDelete(Object entity) {
		Receiving receiving = (Receiving) entity;
		Dialog confirmDialog = new Dialog("Delete Receiving");
		confirmDialog.add(new Paragraph(
				"Are you sure you want to delete receiving record '" + receiving.id + "'?"));

		Button deleteBtn = new Button("Delete", e -> {
			try {
				receivingOrderService.delete(receiving.id);
				Notification.show("Receiving deleted successfully", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				confirmDialog.close();
				refreshData();
			} catch (Exception ex) {
				Notification.show("Failed to delete receiving: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

		confirmDialog.getFooter().add(new Button("Cancel", e -> confirmDialog.close()), deleteBtn);
		confirmDialog.open();
	}

	private void openInspectDialog(ReceivingLine line, Grid<ReceivingLine> linesGrid) {
		Dialog inspectDialog = new Dialog("Quality Inspection");
		ComboBox<String> resultCombo = new ComboBox<>("Result");
		resultCombo.setItems("PASSED", "REJECTED");
		resultCombo.setValue("PASSED");
		TextArea remarksArea = new TextArea("Remarks");
		remarksArea.setWidthFull();

		Button saveBtn = new Button("Save Inspection", e -> {
			try {
				inspectionService.inspect(line.id, resultCombo.getValue(), remarksArea.getValue());
				line.status = "PASSED".equalsIgnoreCase(resultCombo.getValue()) ? "PASSED" : "REJECTED";
				linesGrid.getDataProvider().refreshItem(line);
				Notification.show("Inspection recorded", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				inspectDialog.close();
			} catch (Exception ex) {
				Notification.show("Failed to save inspection: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		inspectDialog.add(new VerticalLayout(resultCombo, remarksArea));
		inspectDialog.getFooter().add(new Button("Cancel", e -> inspectDialog.close()), saveBtn);
		inspectDialog.open();
	}

	private void openPutawayDialog(ReceivingLine line, Grid<ReceivingLine> linesGrid) {
		Dialog putawayDialog = new Dialog("Put Away to Storage");
		ComboBox<Warehouse> warehouseCombo = new ComboBox<>("Target Warehouse");
		warehouseCombo.setItems(warehouseService.findAll(new Warehouse(), Page.ofSize(100)));
		warehouseCombo.setItemLabelGenerator(w -> w.name != null ? w.name : "Warehouse #" + w.id);
		warehouseCombo.setWidthFull();

		Button putawayBtn = new Button("Put Away", e -> {
			try {
				Warehouse wh = warehouseCombo.getValue();
				if (wh == null) {
					Notification.show("Select a target warehouse", 3000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
					return;
				}
				putawayService.executePutaway(line.id, wh.id);
				line.status = "PUTAWAY";
				linesGrid.getDataProvider().refreshItem(line);
				Notification.show("Goods put away and inventory updated", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				putawayDialog.close();
			} catch (Exception ex) {
				Notification.show("Failed to put away: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		putawayBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		putawayDialog.add(new VerticalLayout(warehouseCombo));
		putawayDialog.getFooter().add(new Button("Cancel", e -> putawayDialog.close()), putawayBtn);
		putawayDialog.open();
	}
}
