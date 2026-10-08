package id.putra.core_erp.modules.inbound.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.enums.OrderStatus;
import id.putra.core_erp.modules.inbound.model.Receiving;
import id.putra.core_erp.modules.inbound.service.ReceivingOrderService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "receiving", layout = MainLayout.class)
@PageTitle("Receiving")
@Authenticated
public class ReceivingView extends ViewWithDialog {

	private final ReceivingOrderService receivingOrderService;
	private final Grid<Receiving> grid = new Grid<>();
	private final TextField searchField = new TextField();

	public ReceivingView(ReceivingOrderService receivingOrderService) {
		super();
		this.receivingOrderService = receivingOrderService;

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
		grid.addColumn(r -> r.receivedDate != null ? r.receivedDate.toString() : "-")
				.setHeader("Received Date").setAutoWidth(true);
		grid.addColumn(r -> r.purchaseOrder != null ? r.purchaseOrder.poNumber : "-")
				.setHeader("PO Number").setFlexGrow(1);

		grid.addComponentColumn(receiving -> {
			String status = receiving.status != null ? receiving.status.name() : OrderStatus.COMPLETED.name();
			Span badge = new Span(status);
			badge.getElement().getThemeList().add("badge success");
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		grid.addComponentColumn(receiving -> {
			Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDialog(receiving));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(receiving));
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

			return new HorizontalLayout(editBtn, deleteBtn);
		}).setHeader("Actions").setAutoWidth(true);

		add(grid);

		refreshData();
	}

	@Override
	public void refreshData() {
		var entity = new Receiving();
		grid.setItems(receivingOrderService.findAll(entity, Page.ofSize(100)));
	}

	@Override
	protected void buildAndShowDialog(Object entity) throws Exception {
		Receiving receiving = (Receiving) entity;
		boolean isEdit = receiving.id != null;
		Dialog dialog = new Dialog(isEdit ? "Edit Goods Receipt" : "New Goods Receipt");

		Button saveBtn = new Button("Save", e -> {
			try {
				if (isEdit) {
					receivingOrderService.update(receiving);
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
}
