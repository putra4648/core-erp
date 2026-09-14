package id.putra.core_erp.purchase_requisition.ui.views.category;

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

import id.putra.core_erp.purchase_requisition.service.CategoryService;
import id.putra.core_erp.shared.entity.Category;
import id.putra.core_erp.shared.ui.MainLayout;
import jakarta.annotation.security.PermitAll;

@Route(value = "pr/categories", layout = MainLayout.class)
@PageTitle("PR Categories | CORE ERP")
@PermitAll
public class CategoryView extends VerticalLayout {

    private final CategoryService categoryService;
    private final Grid<Category> grid = new Grid<>();
    private final List<Category> categoryList = new ArrayList<>();

    public CategoryView(CategoryService categoryService) {
        this.categoryService = categoryService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 header = new H2("Category Management");
        header.addClassNames(LumoUtility.FontSize.XLARGE);

        Button addButton = new Button("Add Category", VaadinIcon.PLUS.create(), e -> openCategoryDialog(null));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshGrid());

        HorizontalLayout toolbar = new HorizontalLayout(addButton, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        configureGrid();

        add(header, toolbar, grid);
        refreshGrid();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(c -> c.getId() != null ? c.getId() : "-").setHeader("Category ID").setAutoWidth(true);
        grid.addColumn(c -> c.getName() != null ? c.getName() : "-").setHeader("Category Name").setFlexGrow(2);
        grid.addColumn(c -> c.getParentId() != null ? c.getParentId() : "-").setHeader("Parent Category ID")
                .setFlexGrow(1);

        grid.addComponentColumn(cat -> {
            Button editBtn = new Button("Edit", VaadinIcon.EDIT.create(), e -> openCategoryDialog(cat));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            return editBtn;
        }).setHeader("Actions").setAutoWidth(true);
    }

    private void refreshGrid() {
        try {
            List<Category> res = categoryService.getCategory();
            if (res != null) {
                categoryList.clear();
                categoryList.addAll(flattenCategories(res));
                grid.setItems(categoryList);
            }
        } catch (Exception ex) {
            Notification.show("Failed to load categories: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private List<Category> flattenCategories(List<Category> source) {
        List<Category> flat = new ArrayList<>();
        for (Category c : source) {
            flat.add(c);
            // if (c.getChildren() != null && !c.getChildren().isEmpty()) {
            // flat.addAll(flattenCategories(c.getChildren()));
            // }
        }
        return flat;
    }

    private void openCategoryDialog(Category editCategory) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(editCategory == null ? "Add Category" : "Edit Category");
        dialog.setWidth("450px");

        FormLayout form = new FormLayout();
        TextField nameField = new TextField("Category Name");
        nameField.setRequired(true);

        ComboBox<Category> parentCombo = new ComboBox<>("Parent Category");
        parentCombo.setItems(categoryList);
        parentCombo.setItemLabelGenerator(c -> c.getName());
        parentCombo.setClearButtonVisible(true);

        if (editCategory != null) {
            nameField.setValue(editCategory.getName() != null ? editCategory.getName() : "");
            if (editCategory.getParentId() != null) {
                categoryList.stream()
                        .filter(c -> editCategory.getParentId().equals(c.getId()))
                        .findFirst()
                        .ifPresent(parentCombo::setValue);
            }
        }

        form.add(nameField, parentCombo);

        Button saveBtn = new Button("Save", e -> {
            if (nameField.getValue().isBlank()) {
                Notification.show("Name is required", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            Category dto = new Category();
            dto.setName(nameField.getValue());
            if (parentCombo.getValue() != null) {
                dto.setParentId(parentCombo.getValue().getId());
            }

            try {
                if (editCategory == null) {
                    categoryService.addCategory(dto);
                    Notification.show("Category created", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    dto.setId(editCategory.getId());
                    categoryService.updateCategory(dto);
                    Notification.show("Category updated", 3000, Notification.Position.TOP_CENTER)
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
