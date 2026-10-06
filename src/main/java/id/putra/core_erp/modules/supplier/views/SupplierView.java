package id.putra.core_erp.modules.supplier.views;

import java.util.ArrayList;
import java.util.List;

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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.modules.supplier.service.SupplierService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "master/suppliers", layout = MainLayout.class)
@PageTitle("Suppliers")
@Authenticated
public class SupplierView extends VerticalLayout {

	private final SupplierService supplierService;
	private final Grid<Supplier> grid = new Grid<>();
	private final TextField searchField = new TextField();

	public SupplierView(SupplierService supplierService) {
		this.supplierService = supplierService;
		setSizeFull();
		setPadding(true);
		setSpacing(true);

		addHeader();
		addToolbar();
		configureGrid();
		add(grid);

		refreshData();
	}

	private void addHeader() {
		H2 title = new H2("Supplier Management");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Manage suppliers, vendor profiles, and contacts.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	private void addToolbar() {
		searchField.setPlaceholder("Search suppliers...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.addValueChangeListener(e -> refreshData());

		Button addButton = new Button("Add Supplier", VaadinIcon.PLUS.create(),
				e -> openSupplierDialog(new Supplier()));
		addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshButton = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton, refreshButton);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, searchField);
		add(toolbar);
	}

	private void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(s -> s.getCode() != null ? s.getCode() : "-").setHeader("Supplier Code").setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(s -> s.getName() != null ? s.getName() : "-").setHeader("Supplier Name").setFlexGrow(1)
				.setSortable(true);

<<<<<<< HEAD
        grid.addColumn(s -> {
            if (s.getEmail() != null ) {
                return s.getEmail();
            }
            return "-";
        }).setHeader("Email").setFlexGrow(1);

        grid.addComponentColumn(supplier -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openSupplierDialog(supplier));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
=======
		grid.addColumn(s -> {
			return s.getEmail();
		}).setHeader("Email").setFlexGrow(1);

		grid.addComponentColumn(supplier -> {
			boolean active = Boolean.TRUE.equals(supplier.getIsActive());
			Span badge = new Span(active ? "Active" : "Inactive");
			badge.getElement().getThemeList().add("badge " + (active ? "success" : "error"));
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		grid.addComponentColumn(supplier -> {
			Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openSupplierDialog(supplier));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(supplier));
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

			return new HorizontalLayout(editBtn, deleteBtn);
		}).setHeader("Actions").setAutoWidth(true);
	}

	private void refreshData() {
		Supplier entity = new Supplier();
		entity.setName(searchField.getValue());

		List<Supplier> res = new ArrayList<>();
		supplierService.findAll(entity, Page.ofSize(100)).forEach(res::add);
		grid.setItems(res);
	}

	private void openSupplierDialog(Supplier supplier) {

<<<<<<< HEAD
        TextField codeField = new TextField("Supplier Code");
        TextField nameField = new TextField("Supplier Name");
        TextField emailField = new TextField("Email");

        codeField.setValue(supplier.getCode() != null ? supplier.getCode() : "");
        nameField.setValue(supplier.getName() != null ? supplier.getName() : "");
        emailField.setValue(supplier.getEmail() != null ? supplier.getEmail() : "");


        FormLayout formLayout = new FormLayout(codeField, nameField, emailField);
        dialog.add(formLayout);

        Button saveBtn = new Button("Save", e -> {
            if (validator.validate(supplier).isEmpty()) {
                try {
                    if (isEdit) {
                        supplierService.update(supplier.getId(), supplier);
                        Notification.show("Supplier updated successfully", 3000, Notification.Position.TOP_END)
                                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    } else {
                        supplierService.create(List.of(supplier));
                        Notification.show("Supplier created successfully", 3000, Notification.Position.TOP_END)
                                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    }
                    dialog.close();
                    refreshData();
                } catch (Exception ex) {
                    Notification
                            .show("Failed to save supplier: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
=======
		Dialog dialog = new Dialog("Supplier");

		TextField codeField = new TextField("Supplier Code");
		TextField nameField = new TextField("Supplier Name");
		Checkbox activeCheck = new Checkbox("Is Active");
		TextField emailField = new TextField("Email");

		codeField.setValue(supplier.getCode() != null ? supplier.getCode() : "");
		nameField.setValue(supplier.getName() != null ? supplier.getName() : "");
		activeCheck.setValue(supplier.getIsActive() != null ? supplier.getIsActive() : true);
		emailField.setValue(supplier.getEmail() != null ? supplier.getEmail() : "");

		FormLayout formLayout = new FormLayout(codeField, nameField, activeCheck, emailField);
		dialog.add(formLayout);

		Button saveBtn = new Button("Save", e -> {
				try {
					var entity = new Supplier();
					entity.id = supplier.id;
					entity.setName(nameField.getValue());
					entity.setCode(codeField.getValue());
					entity.setIsActive(activeCheck.getValue());
					entity.setEmail(emailField.getValue());

					if (supplier.id != null) {
						supplierService.update(entity);
						Notification.show("Supplier updated successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					} else {
						supplierService.save(entity);
						Notification.show("Supplier created successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					}
					dialog.close();
					refreshData();
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
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	private void confirmDelete(Supplier supplier) {
		Dialog confirmDialog = new Dialog("Delete Supplier");
		confirmDialog.add(new Paragraph("Are you sure you want to delete supplier '" + supplier.getName() + "'?"));

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
