package id.putra.core_erp.purchase_requisition.ui.views.items;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

import id.putra.core_erp.purchase_requisition.dto.ItemDto;
import id.putra.core_erp.purchase_requisition.dto.SupplierDto;
import id.putra.core_erp.purchase_requisition.service.CategoryService;
import id.putra.core_erp.purchase_requisition.service.ItemService;
import id.putra.core_erp.purchase_requisition.service.SupplierService;
import id.putra.core_erp.shared.ui.MainLayout;
import jakarta.annotation.security.PermitAll;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@Route(value = "items", layout = MainLayout.class)
@PageTitle("Items Catalog | PR System")
@PermitAll
@RegisterReflectionForBinding({ ItemDto.class, SupplierDto.class })
public class ItemView extends VerticalLayout {

    private final ItemService itemService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    private final Grid<ItemDto> grid = new Grid<>();
    private final TextField searchField = new TextField();
    private final ComboBox<String> statusFilter = new ComboBox<>();
    private final List<ItemDto> itemList = new ArrayList<>();

    public ItemView(ItemService itemService, CategoryService categoryService, SupplierService supplierService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 header = new H2("Item Catalog");
        header.addClassNames(LumoUtility.FontSize.XLARGE);

        HorizontalLayout toolbar = createToolbar();
        configureGrid();

        add(header, toolbar, grid);
        refreshGrid();
    }

    private HorizontalLayout createToolbar() {
        searchField.setPlaceholder("Search item by name / code...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("300px");
        searchField.addValueChangeListener(e -> refreshGrid());

        statusFilter.setPlaceholder("Status");
        statusFilter.setItems("ACTIVE", "INACTIVE");
        statusFilter.setValue("ACTIVE");
        statusFilter.setWidth("140px");
        statusFilter.addValueChangeListener(e -> refreshGrid());

        Button addBtn = new Button("Add Item", VaadinIcon.PLUS.create(), e -> openItemDialog(null));
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshGrid());

        HorizontalLayout toolbar = new HorizontalLayout(searchField, statusFilter, addBtn, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        return toolbar;
    }

    private void configureGrid() {
        grid.setSizeFull();

        grid.addColumn(i -> i.getItem_number() != null ? i.getItem_number() : "-").setHeader("Item Code")
                .setAutoWidth(true);
        grid.addColumn(i -> i.getName() != null ? i.getName() : "-").setHeader("Item Name").setFlexGrow(2);
        grid.addColumn(i -> i.getCategory() != null ? i.getCategory() : "-").setHeader("Category").setFlexGrow(1);
        grid.addColumn(i -> i.getSupplier() != null ? i.getSupplier().getName() : "-").setHeader("Supplier")
                .setFlexGrow(1);
        grid.addColumn(i -> i.getPrice() != null ? String.format("%,d", i.getPrice()) : "-").setHeader("Price")
                .setAutoWidth(true);
        grid.addColumn(i -> i.getUnit() != null ? i.getUnit() : "-").setHeader("UOM").setAutoWidth(true);

        grid.addComponentColumn(i -> {
            Span badge = new Span(i.getStatus() != null ? i.getStatus() : "ACTIVE");
            if ("ACTIVE".equalsIgnoreCase(i.getStatus()) || i.getStatus() == null) {
                badge.getElement().getThemeList().add("badge success");
            } else {
                badge.getElement().getThemeList().add("badge error");
            }
            return badge;
        }).setHeader("Status").setAutoWidth(true);

        grid.addComponentColumn(i -> {
            Button editBtn = new Button("Edit", VaadinIcon.EDIT.create(), e -> openItemDialog(i));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            return editBtn;
        }).setHeader("Actions").setAutoWidth(true);
    }

    private void refreshGrid() {
        try {
            String search = searchField.getValue() != null ? searchField.getValue() : "";
            String status = statusFilter.getValue() != null ? statusFilter.getValue() : "ACTIVE";

            List<ItemDto> items = itemService.getItems(search, status, "", 1, 100, "name", "asc");
            itemList.clear();
            if (items != null) {
                itemList.addAll(items);
            }
            grid.setItems(itemList);
        } catch (Exception ex) {
            Notification.show("Failed to load items: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void openItemDialog(ItemDto editItem) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(editItem == null ? "Add Item" : "Edit Item");
        dialog.setWidth("600px");

        FormLayout form = new FormLayout();
        TextField itemNumberField = new TextField("Item Number / Code");
        itemNumberField.setRequired(true);
        TextField nameField = new TextField("Item Name");
        nameField.setRequired(true);
        TextField priceField = new TextField("Estimated Price");
        TextField unitField = new TextField("Unit of Measure (UOM)");
        unitField.setValue("PCS");
        TextField glAccField = new TextField("GL Account");
        TextArea descField = new TextArea("Description");

        ComboBox<String> categoryCombo = new ComboBox<>("Category");
        try {
            var catRes = categoryService.getCategoryDropdown();
            if (catRes != null) {
                categoryCombo.setItems(catRes.stream().map(c -> c.getName()).toList());
            }
        } catch (Exception ignored) {
        }

        ComboBox<SupplierDto> supplierCombo = new ComboBox<>("Supplier");
        try {
            var supRes = supplierService.getSupplierDropdown();
            if (supRes != null) {
                supplierCombo.setItems(supRes);
                supplierCombo.setItemLabelGenerator(s -> s.getName());
            }
        } catch (Exception ignored) {
        }

        if (editItem != null) {
            itemNumberField.setValue(editItem.getItem_number() != null ? editItem.getItem_number() : "");
            itemNumberField.setReadOnly(true);
            nameField.setValue(editItem.getName() != null ? editItem.getName() : "");
            priceField.setValue(editItem.getPrice() != null ? editItem.getPrice().toString() : "0");
            unitField.setValue(editItem.getUnit() != null ? editItem.getUnit() : "PCS");
            glAccField.setValue(editItem.getGl_acc() != null ? editItem.getGl_acc() : "");
            descField.setValue(editItem.getDescription() != null ? editItem.getDescription() : "");
            if (editItem.getCategory() != null) {
                categoryCombo.setValue(editItem.getCategory());
            }
            if (editItem.getSupplier() != null) {
                supplierCombo.setValue(editItem.getSupplier());
            }
        }

        form.add(itemNumberField, nameField, categoryCombo, supplierCombo, priceField, unitField, glAccField,
                descField);
        form.setColspan(descField, 2);

        Button saveBtn = new Button("Save", e -> {
            if (nameField.getValue().isBlank() || itemNumberField.getValue().isBlank()) {
                Notification.show("Item Number and Name are required", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            ItemDto dto = new ItemDto();
            dto.setItem_number(itemNumberField.getValue());
            dto.setName(nameField.getValue());
            dto.setCategory(categoryCombo.getValue());
            if (supplierCombo.getValue() != null) {
                dto.setSupplier_id(supplierCombo.getValue().getId());
                dto.setSupplier(supplierCombo.getValue());
            }
            try {
                dto.setPrice(new BigInteger(priceField.getValue().replaceAll("[^0-9]", "")));
            } catch (Exception ignored) {
                dto.setPrice(BigInteger.ZERO);
            }
            dto.setUnit(unitField.getValue());
            dto.setGl_acc(glAccField.getValue());
            dto.setDescription(descField.getValue());
            dto.setStatus("ACTIVE");

            try {
                if (editItem == null) {
                    itemService.addItem(dto, null, null);
                    Notification.show("Item added successfully", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    itemService.updateItem(dto, null, null);
                    Notification.show("Item updated successfully", 3000, Notification.Position.TOP_CENTER)
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
