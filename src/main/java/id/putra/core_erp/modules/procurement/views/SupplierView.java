package id.putra.core_erp.modules.procurement.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.modules.procurement.service.SupplierService;
import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.shared.enums.StatusEnum;
import id.putra.core_erp.shared.enums.TypeEnum;
import id.putra.core_erp.shared.ui.MainLayout;
import io.quarkus.panache.common.Page;
import jakarta.annotation.security.PermitAll;

@Route(value = "prc/suppliers", layout = MainLayout.class)
@PageTitle("PR Suppliers | CORE ERP")
@PermitAll
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

        grid.addColumn(s -> s.getAccNo() != null ? s.getAccNo() : "-").setHeader("Account No").setAutoWidth(true);
        grid.addColumn(s -> s.getName() != null ? s.getName() : "-").setHeader("Supplier Name").setFlexGrow(2);
        grid.addColumn(s -> s.getPhone() != null ? s.getPhone() : "-").setHeader("Phone").setFlexGrow(1);
        grid.addColumn(s -> s.getCurrency() != null ? s.getCurrency() : "-").setHeader("Currency").setAutoWidth(true);

        grid.addComponentColumn(s -> {
            Span badge = new Span(s.getStatus() != null ? s.getStatus().name() : "ACTIVE");
            if (s.getStatus() == StatusEnum.ACTIVE || s.getStatus() == null) {
                badge.getElement().getThemeList().add("badge success");
            } else {
                badge.getElement().getThemeList().add("badge error");
            }
            return badge;
        }).setHeader("Status").setAutoWidth(true);

        grid.addComponentColumn(s -> {
            Button editBtn = new Button("Edit", VaadinIcon.EDIT.create(), e -> openSupplierDialog(s));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);

            Button toggleBtn = new Button(s.getStatus() == StatusEnum.ACTIVE ? "Deactivate" : "Activate",
                    e -> toggleStatus(s));
            toggleBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);

            HorizontalLayout actions = new HorizontalLayout(editBtn, toggleBtn);
            return actions;
        }).setHeader("Actions").setAutoWidth(true);
    }

    private void refreshGrid() {
        try {
            String search = searchField.getValue() != null ? searchField.getValue() : "";
            String status = statusFilter.getValue() != null ? statusFilter.getValue() : "ACTIVE";

            List<Supplier> res = supplierService.getSupplier(search, status, Page.ofSize(100));
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

    private void toggleStatus(Supplier s) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("id", s.getId());
            supplierService.updateSupplierStatus(body);
            refreshGrid();
            Notification.show("Status updated", 2000, Notification.Position.TOP_CENTER);
        } catch (Exception ex) {
            Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void openSupplierDialog(Supplier editSupplier) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(editSupplier == null ? "Add Supplier" : "Edit Supplier");
        dialog.setWidth("600px");

        FormLayout form = new FormLayout();
        TextField nameField = new TextField("Supplier Name");
        nameField.setRequired(true);
        TextField accNoField = new TextField("Account No");
        TextField phoneField = new TextField("Phone");
        TextField faxField = new TextField("Fax");
        TextField websiteField = new TextField("Website");
        TextField npwpField = new TextField("NPWP");
        TextField accBankField = new TextField("Bank Account");
        TextField currField = new TextField("Currency");
        currField.setValue("IDR");
        TextArea notesField = new TextArea("Notes");

        if (editSupplier != null) {
            nameField.setValue(editSupplier.getName() != null ? editSupplier.getName() : "");
            accNoField.setValue(editSupplier.getAccNo() != null ? editSupplier.getAccNo() : "");
            phoneField.setValue(editSupplier.getPhone() != null ? editSupplier.getPhone() : "");
            faxField.setValue(editSupplier.getFax() != null ? editSupplier.getFax() : "");
            websiteField.setValue(editSupplier.getWebsite() != null ? editSupplier.getWebsite() : "");
            npwpField.setValue(editSupplier.getNpwp() != null ? editSupplier.getNpwp() : "");
            accBankField.setValue(editSupplier.getAccBank() != null ? editSupplier.getAccBank() : "");
            currField.setValue(editSupplier.getCurrency() != null ? editSupplier.getCurrency() : "IDR");
            notesField.setValue(editSupplier.getNotes() != null ? editSupplier.getNotes() : "");
        }

        form.add(nameField, accNoField, phoneField, faxField, websiteField, npwpField, accBankField, currField,
                notesField);
        form.setColspan(notesField, 2);

        Button saveBtn = new Button("Save", e -> {
            if (nameField.getValue().isBlank()) {
                Notification.show("Name is required", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            Supplier dto = new Supplier();
            dto.setName(nameField.getValue());
            dto.setAccNo(accNoField.getValue());
            dto.setPhone(phoneField.getValue());
            dto.setFax(faxField.getValue());
            dto.setWebsite(websiteField.getValue());
            dto.setNpwp(npwpField.getValue());
            dto.setAccBank(accBankField.getValue());
            dto.setCurrency(currField.getValue());
            dto.setNotes(notesField.getValue());
            dto.setType(TypeEnum.TRADE);
            dto.setStatus(StatusEnum.ACTIVE);

            try {
                if (editSupplier == null) {
                    supplierService.addSupplier(dto, null);
                    Notification.show("Supplier added successfully", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    dto.setId(editSupplier.getId());
                    supplierService.updateSupplier(dto, null);
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
