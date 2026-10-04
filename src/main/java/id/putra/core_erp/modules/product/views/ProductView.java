package id.putra.core_erp.modules.product.views;

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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.product.service.ProductService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Validator;

@Route(value = "master/products", layout = MainLayout.class)
@PageTitle("Products")
@Authenticated
public class ProductView extends VerticalLayout {
    @Inject
    Validator validator;

    private final ProductService productService;
    private final Grid<Product> grid = new Grid<>();
    private final TextField searchField = new TextField();
    private Product form;

    public ProductView(ProductService productService) {
        this.productService = productService;
        this.form = new Product();

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
        H2 title = new H2("Product Catalog");
        title.addClassNames("m-0", "text-xl", "font-bold");
        Paragraph desc = new Paragraph("Manage product items, SKU details, and stock thresholds.");
        desc.addClassNames("text-secondary", "m-0");
        add(title, desc);
    }

    private void addToolbar() {
        searchField.setPlaceholder("Search products by name or SKU...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.addValueChangeListener(e -> refreshData());

        Button addButton = new Button("Add Product", VaadinIcon.PLUS.create(),
                e -> openProductDialog(new Product()));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refreshButton = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

        HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton, refreshButton);
        toolbar.setWidthFull();
        toolbar.setFlexGrow(1, searchField);
        add(toolbar);
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(p -> p.getSku() != null ? p.getSku() : "").setHeader("SKU").setAutoWidth(true).setSortable(true);
        grid.addColumn(p -> p.getName() != null ? p.getName() : "").setHeader("Product Name").setFlexGrow(1)
                .setSortable(true);
        grid.addColumn(p -> p.getQuantity() != null ? p.getQuantity() : 0).setHeader("Total Stock").setAutoWidth(true);
        grid.addColumn(p -> p.getMinStock() != null ? p.getMinStock() : 0).setHeader("Min Stock").setAutoWidth(true);
        grid.addColumn(p -> p.getMaxStock() != null ? p.getMaxStock() : 0).setHeader("Max Stock").setAutoWidth(true);

        grid.addComponentColumn(product -> {
            boolean active = Boolean.TRUE.equals(product.getIsActive());
            Span badge = new Span(active ? "Active" : "Inactive");
            badge.getElement().getThemeList().add("badge " + (active ? "success" : "error"));
            return badge;
        }).setHeader("Status").setAutoWidth(true);

        grid.addComponentColumn(product -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openProductDialog(product));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(product));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Actions").setAutoWidth(true);
    }

    private void refreshData() {
        Product searchParam = new Product();
        searchParam.setName(searchField.getValue());
        grid.setItems(productService.getProducts(searchParam, Page.ofSize(100)));
    }

    private void openProductDialog(Product product) {
        boolean isEdit = product.id != null;
        Dialog dialog = new Dialog(isEdit ? "Edit Product" : "Add Product");

        TextField skuField = new TextField("SKU", val -> {
            this.form.setSku(val.getValue());
        });
        TextField nameField = new TextField("Product Name", val -> {
            this.form.setName(val.getValue());
        });
        TextArea descField = new TextArea("Description", val -> {
            this.form.setSku(val.getValue());
        });
        IntegerField qtyField = new IntegerField("Quantity", 1, val -> {
            this.form.setQuantity(val.getValue().longValue());
        });
        IntegerField minStockField = new IntegerField("Min Stock", 1, val -> {
            this.form.setMinStock(val.getValue().longValue());
        });
        IntegerField maxStockField = new IntegerField("Max Stock", 1, val -> {
            this.form.setMaxStock(val.getValue().longValue());
        });
        Checkbox activeCheck = new Checkbox("Is Active", val -> {
            this.form.setIsActive(val.getValue());
        });

        skuField.setValue(product.getSku() != null ? product.getSku() : "");
        nameField.setValue(product.getName() != null ? product.getName() : "");
        descField.setValue(product.getDescription() != null ? product.getDescription() : "");
        qtyField.setValue(product.getQuantity() != null ? product.getQuantity().intValue() : 0);
        minStockField.setValue(product.getMinStock() != null ? product.getMinStock().intValue() : 0);
        maxStockField.setValue(product.getMaxStock() != null ? product.getMaxStock().intValue() : 0);
        activeCheck.setValue(product.getIsActive() != null ? product.getIsActive() : true);

        FormLayout formLayout = new FormLayout(skuField, nameField, qtyField, minStockField, maxStockField, activeCheck,
                descField);
        dialog.add(formLayout);

        Button saveBtn = new Button("Save", e -> {
            if (validator.validate(form).isEmpty()) {
                product.setQuantity(qtyField.getValue() != null ? qtyField.getValue().longValue() : 0L);
                product.setMinStock(minStockField.getValue() != null ? minStockField.getValue().longValue() : 0L);
                product.setMaxStock(maxStockField.getValue() != null ? maxStockField.getValue().longValue() : 0L);

                try {
                    if (isEdit) {
                        productService.update(product);
                        Notification.show("Product updated successfully", 3000, Notification.Position.TOP_END)
                                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    } else {
                        productService.save(product);
                        Notification.show("Product created successfully", 3000, Notification.Position.TOP_END)
                                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    }
                    dialog.close();
                    refreshData();
                } catch (Exception ex) {
                    Notification.show("Failed to save product: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

    private void confirmDelete(Product product) {
        Dialog confirmDialog = new Dialog("Delete Product");
        confirmDialog.add(new Paragraph("Are you sure you want to delete product '" + product.getName() + "'?"));

        Button deleteBtn = new Button("Delete", e -> {
            try {
                productService.delete(product.id);
                Notification.show("Product deleted successfully", 3000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                confirmDialog.close();
                refreshData();
            } catch (Exception ex) {
                Notification.show("Failed to delete product: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        Button cancelBtn = new Button("Cancel", e -> confirmDialog.close());

        confirmDialog.getFooter().add(cancelBtn, deleteBtn);
        confirmDialog.open();
    }
}
