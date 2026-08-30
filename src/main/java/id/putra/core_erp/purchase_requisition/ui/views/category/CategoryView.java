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

import id.putra.core_erp.purchase_requisition.dto.CategoryDto;
import id.putra.core_erp.purchase_requisition.service.CategoryService;
import id.putra.core_erp.shared.ui.MainLayout;
import jakarta.annotation.security.PermitAll;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@Route(value = "pr/categories", layout = MainLayout.class)
@PageTitle("PR Categories | CORE ERP")
@PermitAll
@RegisterReflectionForBinding({ CategoryDto.class })
public class CategoryView extends VerticalLayout {

    private final CategoryService categoryService;
    private final Grid<CategoryDto> grid = new Grid<>();
    private final List<CategoryDto> categoryList = new ArrayList<>();

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
        grid.addColumn(c -> c.getParent_id() != null ? c.getParent_id() : "-").setHeader("Parent Category ID")
                .setFlexGrow(1);

        grid.addComponentColumn(cat -> {
            Button editBtn = new Button("Edit", VaadinIcon.EDIT.create(), e -> openCategoryDialog(cat));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            return editBtn;
        }).setHeader("Actions").setAutoWidth(true);
    }

    private void refreshGrid() {
        try {
            List<CategoryDto> res = categoryService.getCategory();
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

    private List<CategoryDto> flattenCategories(List<CategoryDto> source) {
        List<CategoryDto> flat = new ArrayList<>();
        for (CategoryDto c : source) {
            flat.add(c);
            if (c.getChildren() != null && !c.getChildren().isEmpty()) {
                flat.addAll(flattenCategories(c.getChildren()));
            }
        }
        return flat;
    }

    private void openCategoryDialog(CategoryDto editCategory) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(editCategory == null ? "Add Category" : "Edit Category");
        dialog.setWidth("450px");

        FormLayout form = new FormLayout();
        TextField nameField = new TextField("Category Name");
        nameField.setRequired(true);

        ComboBox<CategoryDto> parentCombo = new ComboBox<>("Parent Category");
        parentCombo.setItems(categoryList);
        parentCombo.setItemLabelGenerator(c -> c.getName());
        parentCombo.setClearButtonVisible(true);

        if (editCategory != null) {
            nameField.setValue(editCategory.getName() != null ? editCategory.getName() : "");
            if (editCategory.getParent_id() != null) {
                categoryList.stream()
                        .filter(c -> editCategory.getParent_id().equals(c.getId()))
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

            CategoryDto dto = new CategoryDto();
            dto.setName(nameField.getValue());
            if (parentCombo.getValue() != null) {
                dto.setParent_id(parentCombo.getValue().getId());
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
