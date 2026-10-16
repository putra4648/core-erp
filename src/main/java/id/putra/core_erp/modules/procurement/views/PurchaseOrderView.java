package id.putra.core_erp.modules.procurement.views;

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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.inbound.service.PurchaseOrderService;
import id.putra.core_erp.modules.inbound.service.ReceivingOrderService;
import id.putra.core_erp.modules.procurement.model.PurchaseOrder;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "po", layout = MainLayout.class)
@PageTitle("Purchase Orders")
@Authenticated
public class PurchaseOrderView extends ViewWithDialog {

	private final PurchaseOrderService purchaseOrderService;
	private final ReceivingOrderService receivingOrderService;
	private final Grid<PurchaseOrder> grid = new Grid<>();
	private final TextField searchField = new TextField();

	private Binder<PurchaseOrder> binder = new BeanValidationBinder<>(PurchaseOrder.class);
	private TextField poNumber = new TextField("PO Number");
	private DatePicker deliveryDate = new DatePicker("Delivery Date");

	public PurchaseOrderView(PurchaseOrderService purchaseOrderService,
			ReceivingOrderService receivingOrderService) {
		super();
		this.purchaseOrderService = purchaseOrderService;
		this.receivingOrderService = receivingOrderService;

		configureValidation();

		addHeader();
		addToolbar();
		configureGrid();

	}

	private void configureValidation() {
		binder.bind(poNumber, "poNumber");
	}

	@Override
	public void addHeader() {
		H2 title = new H2("Purchase Orders");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Track inbound vendor purchase orders and item lines.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search PO number...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.addValueChangeListener(e -> refreshData());

		Button addButton = new Button("New Purchase Order", VaadinIcon.PLUS.create(),
				e -> openDialog(new PurchaseOrder()));
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
		grid.addColumn(p -> p.poNumber != null ? p.poNumber : "").setHeader("PO Number").setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(p -> p.orderDate != null ? p.orderDate.toString() : "-").setHeader("Order Date")
				.setAutoWidth(true);
		grid.addColumn(p -> p.supplier != null ? p.supplier.name : "-").setHeader("Supplier")
				.setFlexGrow(1);
		grid.addColumn(p -> p.quotation != null
				? (p.quotation.rfqNumber != null ? p.quotation.rfqNumber : "RFQ-" + p.quotation.id)
				: "-").setHeader("Source RFQ").setAutoWidth(true);
		grid.addColumn(p -> p.deliveryDate != null ? p.deliveryDate.toString() : "-").setHeader("Delivery Date")
				.setAutoWidth(true);

		grid.addComponentColumn(po -> {
			String status = po.status != null ? po.status.name() : "DRAFT";
			Span badge = new Span(status);
			badge.getElement().getThemeList().add("badge " + ("APPROVED".equals(status) ? "success" : "neutral"));
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		grid.addComponentColumn(po -> {
			Button receiveBtn = new Button("Receive Goods", VaadinIcon.INBOX.create(), e -> receiveGoods(po));
			receiveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
			if (po.purchaseOrderLines == null || po.purchaseOrderLines.isEmpty()) {
				receiveBtn.setEnabled(false);
				receiveBtn.setText("No Lines");
			}
			return receiveBtn;
		}).setHeader("Action").setAutoWidth(true);

		add(grid);

		refreshData();
	}

	@Override
	public void refreshData() {
		PurchaseOrder searchParam = new PurchaseOrder();
		searchParam.poNumber = searchField.getValue();
		grid.setItems(purchaseOrderService.findAll(searchParam, Page.ofSize(100)));
	}

	@Override
	protected void buildAndShowDialog(Object entity) throws Exception {
		PurchaseOrder po = entity != null ? (PurchaseOrder) entity : new PurchaseOrder();
		binder.readBean(po);
		deliveryDate.clear();
		if (po.id != null && po.deliveryDate != null) {
			deliveryDate.setValue(po.deliveryDate);
		}

		Dialog dialog = new Dialog(po.id == null ? "New Purchase Order" : "Edit Purchase Order");
		dialog.setWidth("500px");

		FormLayout form = new FormLayout(poNumber, deliveryDate);
		dialog.add(form);

		Button saveBtn = new Button("Save", e -> {
			try {
				if (binder.writeBeanIfValid(po)) {
					po.deliveryDate = deliveryDate.getValue();
					if (po.id != null) {
						purchaseOrderService.update(po);
						Notification.show("PO updated successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					} else {
						purchaseOrderService.save(po);
						Notification.show("PO created successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					}
					dialog.close();
					refreshData();
				} else {
					Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			} catch (Exception ex) {
				Notification.show("Failed to save PO: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		dialog.getFooter().add(new Button("Cancel", ev -> dialog.close()), saveBtn);
		dialog.open();
	}

	@Override
	public void confirmDelete(Object object) {
		PurchaseOrder po = (PurchaseOrder) object;
		Dialog confirmDialog = new Dialog("Delete Purchase Order");
		confirmDialog.add(new Paragraph("Are you sure you want to delete PO '" + po.poNumber + "'?"));

		Button deleteBtn = new Button("Delete", e -> {
			try {
				purchaseOrderService.delete(po.id);
				Notification.show("PO deleted successfully", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				confirmDialog.close();
				refreshData();
			} catch (Exception ex) {
				Notification.show("Failed to delete PO: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

		confirmDialog.getFooter().add(new Button("Cancel", e -> confirmDialog.close()), deleteBtn);
		confirmDialog.open();
	}

	private void receiveGoods(PurchaseOrder po) {
		try {
			var receiving = receivingOrderService.generateFromPurchaseOrder(po.id);
			Notification.show("Goods receipt "
					+ (receiving.receivingNumber != null ? receiving.receivingNumber : "") + " created from "
					+ po.poNumber, 4000, Notification.Position.TOP_END)
					.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			refreshData();
		} catch (Exception ex) {
			Notification.show("Failed to create goods receipt: " + ex.getMessage(), 4000,
					Notification.Position.TOP_END).addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
}
