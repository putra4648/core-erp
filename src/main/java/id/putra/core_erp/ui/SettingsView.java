package id.putra.core_erp.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.service.DepartmentService;
import id.putra.core_erp.modules.parameter.model.ParameterLookup;
import id.putra.core_erp.modules.parameter.service.ParameterLookupService;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Route(value = "settings", layout = MainLayout.class)
@PageTitle("Settings")
@Authenticated
public class SettingsView extends VerticalLayout {

    @Inject
    Validator validator;

    private final ParameterLookupService parameterLookupService;
    private final DepartmentService departmentService;
    private final TabSheet tabs = new TabSheet();
    private final Grid<ParameterLookup> gridParameterLookup = new Grid<>();
    private final Grid<Department> gridDepartment = new Grid<>();

    public SettingsView(ParameterLookupService parameterLookupService, DepartmentService departmentService) {
        this.parameterLookupService = parameterLookupService;
        this.departmentService = departmentService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        addHeader();
        add(tabs);
        initTab();
    }

    private void initTab() {
    	this.tabs.setSizeFull();
        this.tabs.add("System Parameter", parameterComponent());
        this.tabs.add("Department", departmentComponent());
    }

    private void addHeader() {
        H2 title = new H2("System Settings");
        title.addClassNames("m-0", "text-xl", "font-bold");
        Paragraph desc = new Paragraph("Configure application parameters, dark mode, and system defaults.");
        desc.addClassNames("text-secondary", "m-0");
        add(title, desc);
    }

    private void refreshData(TextField searchField, Object entity) throws Exception {
        String searchTerm = searchField.getValue() != null ? searchField.getValue() : "";

        if (entity instanceof ParameterLookup parsedEntity) {
            parsedEntity.setKey(searchTerm);

            List<ParameterLookup> result = new ArrayList<>();
            parameterLookupService.findAll(parsedEntity, Page.ofSize(100)).forEach(result::add);
            this.gridParameterLookup.setItems(result);

        } else if (entity instanceof Department parsedEntity) {
            parsedEntity.setName(searchTerm);

            List<Department> result = new ArrayList<>();
            departmentService.findAll(parsedEntity, Page.ofSize(100)).forEach(result::add);
            this.gridDepartment.setItems(result);
        } else {
            throw new Exception("Entity is not defined");
        }
    }

    private Component parameterComponent() {
        gridParameterLookup.setSizeFull();
        gridParameterLookup.addColumn(ParameterLookup::getKey).setHeader("Key").setKey("key").setSortable(true).setAutoWidth(true);
        gridParameterLookup.addColumn(ParameterLookup::getValue).setHeader("Value").setKey("value").setSortable(true).setAutoWidth(true);
        gridParameterLookup.addColumn(ParameterLookup::getDescription).setHeader("Description").setKey("description").setSortable(true).setFlexGrow(1);

        gridParameterLookup.addComponentColumn(param -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openParameterDialog(param));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(param));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Actions").setAutoWidth(true);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search parameters...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("280px");
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> loadParameterData(searchField));

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(true);

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create(), e -> openParameterDialog(new ParameterLookup()));
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
            searchField.clear();
            loadParameterData(searchField);
        });

        HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(toolbar, gridParameterLookup);

        // Load data awal
        loadParameterData(searchField);

        return layout;
    }

    private Component departmentComponent() {
        gridDepartment.setSizeFull();
        gridDepartment.addColumn(Department::getCode).setHeader("Code").setKey("code").setSortable(true).setAutoWidth(true);
        gridDepartment.addColumn(Department::getName).setHeader("Name").setKey("name").setSortable(true).setAutoWidth(true);

        gridDepartment.addComponentColumn(param -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDepartmentDialog(param));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(param));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Actions").setAutoWidth(true);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search department...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("280px");
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> loadDepartmentData(searchField));

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(true);

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create(), e -> openDepartmentDialog(new Department()));
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
            searchField.clear();
            loadDepartmentData(searchField);
        });

        HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(toolbar, gridDepartment);

        // Load data awal
        loadDepartmentData(searchField);

        return layout;
    }

    private void loadParameterData(TextField searchField) {
        try {
            refreshData(searchField, new ParameterLookup());
        } catch (Exception ex) {
            Notification.show(ex.getMessage(), 4000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void loadDepartmentData(TextField searchField) {
        try {
            refreshData(searchField, new Department());
        } catch (Exception ex) {
            Notification.show(ex.getMessage(), 4000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void openParameterDialog(ParameterLookup parameter) {
        boolean isEdit = parameter.id != null;
        Dialog dialog = new Dialog("Parameter");

        FormLayout parameterForm = new FormLayout();

        TextField keyField = new TextField("Key");
        keyField.setRequired(true);
        keyField.setValue(parameter.getKey() != null ? parameter.getKey() : "");

        TextField valueField = new TextField("Value");
        valueField.setRequired(true);
        valueField.setValue(parameter.getValue() != null ? parameter.getValue() : "");

        TextField descriptionField = new TextField("Description");
        descriptionField.setRequired(true);
        descriptionField.setValue(parameter.getDescription() != null ? parameter.getDescription() : "");

        parameterForm.add(keyField, valueField, descriptionField);
        dialog.add(parameterForm);

        Button saveBtn = new Button("Save", e -> {
            parameter.setKey(keyField.getValue());
            parameter.setValue(valueField.getValue());
            parameter.setDescription(descriptionField.getValue());

            Set<ConstraintViolation<ParameterLookup>> violations = validator.validate(parameter);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<ParameterLookup> violation : violations) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(violation.getPropertyPath()).append(" ").append(violation.getMessage());
                }
                Notification.show("Validation failed: " + sb, 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                if (isEdit) {
                    parameterLookupService.update(parameter);
                    Notification.show("Parameter updated successfully", 3000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    parameterLookupService.save(parameter);
                    Notification.show("Parameter created successfully", 3000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
                dialog.close();
                gridParameterLookup.getDataProvider().refreshAll();
            } catch (Exception ex) {
                Notification.show("Failed to save parameter: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

    private void openDepartmentDialog(Department dept) {
        boolean isEdit = dept.id != null;
        Dialog dialog = new Dialog("Department");

        FormLayout deptForm = new FormLayout();

        TextField codeField = new TextField("Code");
        codeField.setRequired(true);
        codeField.setValue(dept.getCode() != null ? dept.getCode() : "");

        TextField nameField = new TextField("Name");
        nameField.setRequired(true);
        nameField.setValue(dept.getName() != null ? dept.getName() : "");

        deptForm.add(codeField, nameField);
        dialog.add(deptForm);

        Button saveBtn = new Button("Save", e -> {
            dept.setCode(codeField.getValue());
            dept.setName(nameField.getValue());

            Set<ConstraintViolation<Department>> violations = validator.validate(dept);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<Department> violation : violations) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(violation.getPropertyPath()).append(" ").append(violation.getMessage());
                }
                Notification.show("Validation failed: " + sb, 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                if (isEdit) {
                    departmentService.update(dept);
                    Notification.show("Department updated successfully", 3000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    departmentService.save(dept);
                    Notification.show("Department created successfully", 3000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
                dialog.close();
                gridDepartment.getDataProvider().refreshAll();
            } catch (Exception ex) {
                Notification.show("Failed to save Department: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

    private void confirmDelete(Object parameter) {
        Dialog confirmDialog = new Dialog("Delete");
        confirmDialog.add(new Paragraph("Are you sure you want to delete this data?"));

        Button deleteBtn = new Button("Delete", e -> {
            try {
                String message = "";
                if (parameter instanceof ParameterLookup param) {
                    parameterLookupService.delete(param.id);
                    message = "Parameter";
                } else if (parameter instanceof Department dept) {
                    departmentService.delete(dept.id);
                    message = "Department";
                } else {
                    throw new Exception("Object is not defined");
                }
                Notification.show(message + " deleted successfully", 3000, Notification.Position.TOP_END)
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
}