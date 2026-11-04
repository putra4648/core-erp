package id.putra.core_erp.modules.setting.views.components;

import java.util.List;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.service.DepartmentService;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;

public class DepartmentComponent extends ViewWithDialog {
    private final Grid<Department> grid = new Grid<>();

    private final Binder<@NonNull Department> binder = new BeanValidationBinder<>(Department.class);

    private final TextField searchField = new TextField();
    private final TextField code = new TextField("Code");
    private final TextField name = new TextField("Name");

    private final DepartmentService departmentService;

    public DepartmentComponent(DepartmentService departmentService) {
        this.departmentService = departmentService;

        setSizeFull();
        setPadding(false);
        setSpacing(true);

        configureValidation();
        addToolbar();
        configureGrid();
    }

    private void configureValidation() {
        binder.bind(code, "code");
        binder.bind(name, "name");
    }

    @Override
    public void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(d -> d.code).setHeader("Code").setKey("code").setSortable(true).setAutoWidth(true);
        grid.addColumn(d -> d.name).setHeader("Name").setKey("name").setSortable(true).setAutoWidth(true);

        grid.addComponentColumn(param -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDialog(param));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(param));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Actions").setAutoWidth(true);

        refreshData();

        add(grid);

    }

    @Override
    public void addToolbar() {
        searchField.setPlaceholder("Search department...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("280px");
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> refreshData());

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create(), e -> openDialog(new Department()));
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
            searchField.clear();
            refreshData();
        });

        HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        add(toolbar);
    }

    @Override
    protected void buildAndShowDialog(@NonNull Object entity) throws Exception {
        Department d = (Department) entity;
        binder.readBean(d);
        boolean isEdit = d.id != null;
        Dialog dialog = new Dialog(isEdit ? "Edit Department" : "Add Department");

        FormLayout deptForm = new FormLayout(code, name);
        dialog.add(deptForm);

        Button saveBtn = new Button("Save", e -> {
            try {
                if (binder.writeBeanIfValid(d)) {
                    if (isEdit) {
                        departmentService.update(d);
                        Notification.show("Department updated successfully", 3000, Notification.Position.TOP_END)
                                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    } else {
                        departmentService.save(d);
                        Notification.show("Department created successfully", 3000, Notification.Position.TOP_END)
                                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    }
                    dialog.close();
                    refreshData();
                } else {
                    Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } catch (Exception ex) {
                Notification.show("Failed to save Department: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", ev -> dialog.close());

        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

    @Override
    public void refreshData() {
        Department entity = new Department();
        entity.name = searchField.getValue();
        List<Department> data = departmentService.findAll(entity, Page.of(0, 100));
        grid.setItems(data);
    }

    @Override
    public void confirmDelete(Object entity) {
        Department department = (Department) entity;
        Dialog confirmDialog = new Dialog("Delete");
        confirmDialog.add(new Paragraph("Are you sure you want to delete this data?"));

        Button deleteBtn = new Button("Delete", e -> {
            try {
                departmentService.delete(department.id);
                Notification.show("Department deleted successfully", 3000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                confirmDialog.close();
            } catch (Exception ex) {
                Notification.show("Failed to delete: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        Button cancelBtn = new Button("Cancel", e -> confirmDialog.close());

        confirmDialog.getFooter().add(cancelBtn, deleteBtn);
        confirmDialog.open();
    }

    @Override
    public void addHeader() {
    }

}
