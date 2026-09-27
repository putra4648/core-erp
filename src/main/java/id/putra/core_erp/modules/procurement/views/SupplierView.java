package id.putra.core_erp.modules.procurement.views;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.enums.TypeEnum;
import id.putra.core_erp.modules.procurement.service.SupplierService;
import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "suppliers", layout = MainLayout.class)
@PageTitle("PR Suppliers | CORE ERP")
@Authenticated
public class SupplierView extends VerticalLayout {

    private final SupplierService supplierService;

    private final TextField searchField = new TextField();
    private final ComboBox<String> statusFilter = new ComboBox<>();
    private final Grid<Supplier> grid = new Grid<>();
    private final List<Supplier> supplierList = new ArrayList<>();

    public SupplierView(SupplierService supplierService) {
        this.supplierService = supplierService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 header = new H2("Supplier Directory");
        header.addClassNames(LumoUtility.FontSize.XLARGE);

        Button addBtn = new Button("Add Supplier", VaadinIcon.PLUS.create(), e -> openSupplierDialog(null));
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        searchField.setPlaceholder("Search suppliers...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("280px");
        searchField.addValueChangeListener(e -> refreshGrid());

        statusFilter.setPlaceholder("Status");
        statusFilter.setItems("ACTIVE", "INACTIVE");
        statusFilter.setValue("ACTIVE");
        statusFilter.setWidth("140px");
        statusFilter.addValueChangeListener(e -> refreshGrid());

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshGrid());

        HorizontalLayout toolbar = new HorizontalLayout(searchField, statusFilter, addBtn, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        configureGrid();

        add(header, toolbar, grid);
        refreshGrid();
    }

    private void configureGrid() {
        grid.setSizeFull();

        grid.addColumn(s -> s.getName() != null ? s.getName() : "-").setHeader("Supplier Name").setFlexGrow(2);
        grid.addColumn(s -> s.getPhone() != null ? s.getPhone() : "-").setHeader("Phone").setFlexGrow(1);

        grid.addComponentColumn(s -> {
            Button editBtn = new Button("Edit", VaadinIcon.EDIT.create(), e -> openSupplierDialog(s));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);

            HorizontalLayout actions = new HorizontalLayout(editBtn);
            return actions;
        }).setHeader("Actions").setAutoWidth(true);
    }

    private void refreshGrid() {
        try {
            var params = new Supplier();
            params.setName(searchField.getValue());

            List<Supplier> res = supplierService.findAll(params, Page.ofSize(100));
            supplierList.clear();
            if (res != null) {
                supplierList.addAll(res);
            }
            grid.setItems(supplierList);
        } catch (Exception ex) {
            Notification.show("Failed to load suppliers: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void openSupplierDialog(Supplier editSupplier) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(editSupplier == null ? "Add Supplier" : "Edit Supplier");
        dialog.setWidth("600px");

        FormLayout form = new FormLayout();
        TextField nameField = new TextField("Supplier Name");
        nameField.setRequired(true);
        TextField phoneField = new TextField("Phone");

        if (editSupplier != null) {
            nameField.setValue(editSupplier.getName() != null ? editSupplier.getName() : "");
        }

        form.add(nameField, phoneField);

        Button saveBtn = new Button("Save", e -> {
            if (nameField.getValue().isBlank()) {
                Notification.show("Name is required", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            Supplier dto = new Supplier();
            dto.setName(nameField.getValue());
            dto.setPhone(phoneField.getValue());
            dto.setType(TypeEnum.TRADE);

            try {
                if (editSupplier == null) {
                    supplierService.save(dto);
                    Notification.show("Supplier added successfully", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    dto.setId(editSupplier.getId());
                    supplierService.update(dto);
                    Notification.show("Supplier updated successfully", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
                dialog.close();
                refreshGrid();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.add(form);
        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }
}
