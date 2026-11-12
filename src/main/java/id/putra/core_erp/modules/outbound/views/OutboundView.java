package id.putra.core_erp.modules.outbound.views;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

import id.putra.core_erp.modules.outbound.model.SalesOrder;
import id.putra.core_erp.modules.outbound.service.SalesOrderService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.panache.common.Page;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.security.Authenticated;

@Route(value = "outbound", layout = MainLayout.class)
@PageTitle("Outbound")
@Authenticated
public class OutboundView extends ViewWithDialog {

	private final SalesOrderService salesOrderService;
	private final Grid<SalesOrder> grid = new Grid<>();
	private final TextField searchField = new TextField();

	private Binder<@NonNull SalesOrder> binder = new BeanValidationBinder<>(SalesOrder.class);
	private TextField soNumber = new TextField("Sales Order Number");

	public OutboundView(SalesOrderService salesOrderService) {
		super();
		this.salesOrderService = salesOrderService;

		configureValidation();

		addHeader();
		addToolbar();
		configureGrid();
		add(grid);
	}

	private void configureValidation() {
		binder.bind(soNumber, "soNumber");
	}

	@Override
	public void addHeader() {
		H2 title = new H2("Outbound Management");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Manage customer sales orders, order picking, packing, and shipping.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search Sales Order number...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.addValueChangeListener(e -> refreshData());

		Button addButton = new Button("New Outbound Order", VaadinIcon.PLUS.create(),
				e -> openDialog(new SalesOrder()));
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
		grid.addColumn(so -> so.soNumber != null ? so.soNumber : "").setHeader("SO Number").setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(so -> so.orderDate != null ? so.orderDate.toString() : "-").setHeader("Order Date")
				.setAutoWidth(true);
		grid.addColumn(so -> so.customer != null
				? (so.customer.name != null ? so.customer.name : "Customer #" + so.customer.id)
				: "-")
				.setHeader("Customer").setFlexGrow(1);

		grid.addComponentColumn(so -> {
			String status = so.status != null ? so.status : "PENDING";
			Span badge = new Span(status);
			badge.getElement().getThemeList().add("badge " + ("SHIPPED".equals(status) ? "success" : "contrast"));
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		grid.addComponentColumn(so -> {
			Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDialog(so));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(so));
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

			return new HorizontalLayout(editBtn, deleteBtn);
		}).setHeader("Actions").setAutoWidth(true);

		refreshData();
	}

	@Override
	public void refreshData() {
		var entity = new SalesOrder();
		entity.soNumber = searchField.getValue();

		grid.setItems(salesOrderService.findAll(entity, Page.ofSize(100)));
	}

	@Override
	protected void buildAndShowDialog(@NonNull Object entity) throws Exception {
		SalesOrder order = entity != null ? (SalesOrder) entity : new SalesOrder();
		binder.readBean(order);

		Dialog dialog = new Dialog(order.id == null ? "New Outbound Order" : "Edit Outbound Order");
		dialog.setWidth("500px");

		FormLayout form = new FormLayout(soNumber);
		dialog.add(form);

		Button saveBtn = new Button("Save Order", e -> {
			try {
				if (binder.writeBeanIfValid(order)) {
					if (order.id != null) {
						salesOrderService.update(order);
						Notification.show("Sales order updated successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					} else {
						salesOrderService.save(order);
						Notification.show("Sales order saved successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					}
					dialog.close();
					refreshData();
				} else {
					Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			} catch (Exception ex) {
				Notification.show("Failed to save order: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		dialog.getFooter().add(new Button("Cancel", ev -> dialog.close()), saveBtn);
		dialog.open();
	}

	@Override
	public void confirmDelete(Object entity) {
		SalesOrder order = (SalesOrder) entity;
		Dialog confirmDialog = new Dialog("Delete Outbound Order");
		confirmDialog.add(new Paragraph("Are you sure you want to delete sales order '" + order.soNumber + "'?"));

		Button deleteBtn = new Button("Delete", e -> {
			try {
				salesOrderService.delete(order.id);
				Notification.show("Sales order deleted successfully", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				confirmDialog.close();
				refreshData();
			} catch (Exception ex) {
				Notification.show("Failed to delete order: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

		confirmDialog.getFooter().add(new Button("Cancel", e -> confirmDialog.close()), deleteBtn);
		confirmDialog.open();
	}
}
