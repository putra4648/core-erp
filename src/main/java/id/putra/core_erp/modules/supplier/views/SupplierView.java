package id.putra.core_erp.modules.supplier.views;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.modules.supplier.service.SupplierService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "master/suppliers", layout = MainLayout.class)
@PageTitle("Suppliers")
@Authenticated
public class SupplierView extends ViewWithDialog {

	private final SupplierService supplierService;
	private final Grid<Supplier> grid = new Grid<>();
	private final TextField searchField = new TextField();

	private Binder<@NonNull Supplier> binder = new BeanValidationBinder<>(Supplier.class);
	private TextField code = new TextField("Supplier Code");
	private TextField name = new TextField("Supplier Name");
	private TextField email = new TextField("Email");

	public SupplierView(SupplierService supplierService) {
		this.supplierService = supplierService;

		configureValidation();

		addHeader();
		addToolbar();
		configureGrid();

	}

	private void configureValidation() {
		binder.bind(code, "code");
		binder.bind(name, "name");
		binder.bind(email, "email");
	}

	@Override
	public void addHeader() {
		H2 title = new H2("Supplier Management");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Manage suppliers, vendor profiles, and contacts.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search suppliers...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.addValueChangeListener(e -> refreshData());

		Button addButton = new Button("Add Supplier", VaadinIcon.PLUS.create(), e -> openDialog(new Supplier()));
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
		grid.addColumn(s -> s.code != null ? s.code : "-").setHeader("Code").setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(s -> s.name != null ? s.name : "-").setHeader("Name").setFlexGrow(1)
				.setSortable(true);

		grid.addColumn(s -> {
			return s.email;
		}).setHeader("Email").setFlexGrow(1);

		grid.addComponentColumn(supplier -> {
			Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDialog(supplier));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(supplier));
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

			return new HorizontalLayout(editBtn, deleteBtn);
		}).setHeader("Actions").setAutoWidth(true);

		add(grid);

		refreshData();
	}

	@Override
	public void refreshData() {
		Supplier entity = new Supplier();
		entity.name = searchField.getValue();

		grid.setItems(supplierService.findAll(entity, Page.ofSize(100)));
	}

	@Override
	protected void buildAndShowDialog(Object entity) throws Exception {
		Supplier supplier = entity != null ? (Supplier) entity : new Supplier();
		binder.readBean(supplier);

		Dialog dialog = new Dialog(supplier.id == null ? "Add Supplier" : "Edit Supplier");
		dialog.setWidth("500px");

		FormLayout formLayout = new FormLayout(code, name, email);
		dialog.add(formLayout);

		Button saveBtn = new Button("Save", e -> {
			try {
				if (binder.writeBeanIfValid(supplier)) {
					if (supplier.id != null) {
						supplierService.update(supplier);
						Notification.show("Supplier updated successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					} else {
						supplierService.save(supplier);
						Notification.show("Supplier created successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					}
					dialog.close();
					refreshData();
				} else {
					Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			} catch (Exception ex) {
				Notification
						.show("Failed to save supplier: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}

		});
		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button cancelBtn = new Button("Cancel", e -> dialog.close());

		dialog.getFooter().add(cancelBtn, saveBtn);
		dialog.open();
	}

	@Override
	public void confirmDelete(Object entity) {
		Supplier supplier = (Supplier) entity;

		Dialog confirmDialog = new Dialog("Delete Supplier");
		confirmDialog.add(new Paragraph("Are you sure you want to delete supplier '" + supplier.name + "'?"));

		Button deleteBtn = new Button("Delete", e -> {
			try {
				supplierService.delete(supplier.id);
				Notification.show("Supplier deleted successfully", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				confirmDialog.close();
				refreshData();
			} catch (Exception ex) {
				Notification.show("Failed to delete supplier: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

		Button cancelBtn = new Button("Cancel", e -> confirmDialog.close());

		confirmDialog.getFooter().add(cancelBtn, deleteBtn);
		confirmDialog.open();
	}
}
