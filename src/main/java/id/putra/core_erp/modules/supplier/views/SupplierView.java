package id.putra.core_erp.modules.supplier.views;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.modules.supplier.model.SupplierContact;
import id.putra.core_erp.modules.supplier.service.SupplierService;
import id.putra.core_erp.shared.ui.MainLayout;
import io.quarkus.panache.common.Page;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Validator;

@Route(value = "master/suppliers", layout = MainLayout.class)
@PageTitle("Suppliers | WMS Pro")
@PermitAll
public class SupplierView extends VerticalLayout {
    @Inject 
    Validator validator;

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

        grid.addColumn(s -> {
            if (s.getContacts() != null && !s.getContacts().isEmpty()) {
                return s.getContacts().get(0).getName();
            }
            return "-";
        }).setHeader("Contact Person").setFlexGrow(1);

        grid.addColumn(s -> {
            if (s.getContacts() != null && !s.getContacts().isEmpty()) {
                return s.getContacts().get(0).getEmail();
            }
            return "-";
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

            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Actions").setAutoWidth(true);
    }

    private void refreshData() {
        grid.setItems(supplierService.getAll(searchField.getValue(), Page.ofSize(100)));
    }

    private void openSupplierDialog(Supplier supplier) {
        boolean isEdit = supplier.getId() != null;
        Dialog dialog = new Dialog(isEdit ? "Edit Supplier" : "Add Supplier");

        if (supplier.getContacts() == null) {
            supplier.setContacts(new ArrayList<>());
        }
        SupplierContact cp = supplier.getContacts().isEmpty() ? new SupplierContact()
                : supplier.getContacts().get(0);

        TextField codeField = new TextField("Supplier Code");
        TextField nameField = new TextField("Supplier Name");
        Checkbox activeCheck = new Checkbox("Is Active");

        TextField cpNameField = new TextField("Contact Name");
        TextField cpEmailField = new TextField("Email");
        TextField cpPhoneField = new TextField("Phone");
        TextArea cpAddressField = new TextArea("Address");

        codeField.setValue(supplier.getCode() != null ? supplier.getCode() : "");
        nameField.setValue(supplier.getName() != null ? supplier.getName() : "");
        activeCheck.setValue(supplier.getIsActive() != null ? supplier.getIsActive() : true);

        cpNameField.setValue(cp.getName() != null ? cp.getName() : "");
        cpEmailField.setValue(cp.getEmail() != null ? cp.getEmail() : "");
        cpPhoneField.setValue(cp.getPhone() != null ? cp.getPhone() : "");
        // cpAddressField.setValue(cp.ge() != null ? cp.getAddress() : "");

        FormLayout formLayout = new FormLayout(codeField, nameField, activeCheck, cpNameField, cpEmailField,
                cpPhoneField, cpAddressField);
        dialog.add(formLayout);

        Button saveBtn = new Button("Save", e -> {
            if (validator.validate(supplier).isEmpty()) {
                cp.setName(cpNameField.getValue());
                cp.setEmail(cpEmailField.getValue());
                cp.setPhone(cpPhoneField.getValue());
                // cp.setAddress(cpAddressField.getValue());

                if (supplier.getContacts().isEmpty()) {
                    supplier.getContacts().add(cp);
                } else {
                    supplier.getContacts().set(0, cp);
                }

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

        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

    private void confirmDelete(Supplier supplier) {
        Dialog confirmDialog = new Dialog("Delete Supplier");
        confirmDialog.add(new Paragraph("Are you sure you want to delete supplier '" + supplier.getName() + "'?"));

        Button deleteBtn = new Button("Delete", e -> {
            try {
                supplierService.delete(supplier.getId());
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
