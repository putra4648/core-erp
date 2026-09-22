package id.putra.core_erp.modules.department.views;

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

import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.service.DepartmentService;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Validator;

@Route("department")
@PageTitle(value = "Master Department")
@Authenticated
public class DepartmentView extends VerticalLayout {
    @Inject
    Validator validator;

    private final DepartmentService departmentService;
    private final Grid<Department> grid = new Grid<>();
    private final TextField searchField = new TextField();
    private final Department form;

    public DepartmentView(DepartmentService departmentService) {
        this.departmentService = departmentService;
        this.form = new Department();

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
        H2 title = new H2("Customer Management");
        title.addClassNames("m-0", "text-xl", "font-bold");
        Paragraph desc = new Paragraph("Manage customer details and contact information.");
        desc.addClassNames("text-secondary", "m-0");
        add(title, desc);
    }

    private void addToolbar() {
        searchField.setPlaceholder("Search customers...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.addValueChangeListener(e -> refreshData());

        Button addButton = new Button("Add Customer", VaadinIcon.PLUS.create(),
                e -> openDepartmentDialog(new Department()));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refreshButton = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

        HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton, refreshButton);
        toolbar.setWidthFull();
        toolbar.setFlexGrow(1, searchField);
        add(toolbar);
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(dept -> dept.getCode() != null ? dept.getCode() : "").setHeader("Code")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(dept -> dept.getName() != null ? dept.getName() : "").setHeader("Name")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addComponentColumn(customer -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDepartmentDialog(customer));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(customer));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Actions").setAutoWidth(true);
    }

    private void refreshData() {
        var dept = new Department();
        dept.setName(searchField.getValue());

        List<Department> result = new ArrayList<Department>();
        departmentService.findAll(dept, Page.ofSize(100)).forEach(result::add);
        grid.setItems(page -> result.stream(), count -> 0);
    }

    private void openDepartmentDialog(Department dept) {
        boolean isEdit = dept.getId() != null;
        Dialog dialog = new Dialog(isEdit ? "Edit Customer" : "Add Customer");

        TextField codeField = new TextField("Customer Code", val -> {
            this.form.setCode(val.getValue());
        });
        TextField nameField = new TextField("Customer Name", val -> {
            this.form.setName(val.getValue());
        });

        codeField.setValue(dept.getCode() != null ? dept.getCode() : "");
        nameField.setValue(dept.getName() != null ? dept.getName() : "");

        FormLayout formLayout = new FormLayout(codeField, nameField);
        dialog.add(formLayout);

        Button saveBtn = new Button("Save", e -> {
            if (validator.validate(form).isEmpty()) {
                try {
                    if (isEdit) {
                        departmentService.update(dept);
                        Notification.show("Customer updated successfully", 3000, Notification.Position.TOP_END)
                                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    } else {
                        departmentService.save(dept);
                        Notification.show("Customer created successfully", 3000, Notification.Position.TOP_END)
                                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    }
                    dialog.close();
                    refreshData();
                } catch (Exception ex) {
                    Notification
                            .show("Failed to save customer: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

    private void confirmDelete(Department customer) {
        Dialog confirmDialog = new Dialog("Delete Customer");
        confirmDialog.add(new Paragraph("Are you sure you want to delete customer '" + customer.getName() + "'?"));

        Button deleteBtn = new Button("Delete", e -> {
            try {
                departmentService.delete(customer.getId());
                Notification.show("Customer deleted successfully", 3000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                confirmDialog.close();
                refreshData();
            } catch (Exception ex) {
                Notification.show("Failed to delete customer: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        Button cancelBtn = new Button("Cancel", e -> confirmDialog.close());

        confirmDialog.getFooter().add(cancelBtn, deleteBtn);
        confirmDialog.open();
    }
}
