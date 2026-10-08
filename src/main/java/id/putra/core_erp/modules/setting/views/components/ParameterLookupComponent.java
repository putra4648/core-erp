package id.putra.core_erp.modules.setting.views.components;

import java.util.List;

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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

import id.putra.core_erp.modules.parameter.model.ParameterLookup;
import id.putra.core_erp.modules.parameter.service.ParameterLookupService;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;

public class ParameterLookupComponent extends ViewWithDialog {
    private final Grid<ParameterLookup> grid = new Grid<>();
    private final Binder<ParameterLookup> binder = new BeanValidationBinder<>(ParameterLookup.class);

    private final TextField searchField = new TextField();
    private final TextField key = new TextField("Key");
    private final TextField value = new TextField("Value");
    private final TextField description = new TextField("Description");

    private final ParameterLookupService parameterLookupService;

    public ParameterLookupComponent(ParameterLookupService parameterLookupService) {
        this.parameterLookupService = parameterLookupService;

        setSizeFull();
        setPadding(false);
        setSpacing(true);

        configureValidation();
        addToolbar();
        configureGrid();
    }

    private void configureValidation() {
        binder.bind(key, "key");
        binder.bind(value, "value");
        binder.bind(description, "description");
    }

    @Override
    public void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(p -> p.key).setHeader("Key").setKey("key").setSortable(true).setAutoWidth(true);
        grid.addColumn(p -> p.value).setHeader("Value").setKey("value").setSortable(true)
                .setAutoWidth(true);
        grid.addColumn(p -> p.description).setHeader("Description").setKey("description")
                .setSortable(true).setFlexGrow(1);

        grid.addComponentColumn(param -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDialog(param));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(param));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Actions").setAutoWidth(true);

        add(grid);
    }

    @Override
    public void addHeader() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addHeader'");
    }

    @Override
    public void addToolbar() {
        searchField.setPlaceholder("Search parameters...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("280px");
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> refreshData());

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create(), e -> openDialog(new ParameterLookup()));
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
    public void refreshData() {
        ParameterLookup entity = new ParameterLookup();
        if (!searchField.isEmpty()) {
            entity.key = "ILIKE %" + searchField.getValue() + "%";
            entity.value = "ILIKE %" + searchField.getValue() + "%";
            entity.description = "ILIKE %" + searchField.getValue() + "%";
        }
        List<ParameterLookup> data = parameterLookupService.findAll(entity, Page.of(0, 100));
        grid.setItems(data);
    }

    @Override
    protected void buildAndShowDialog(Object entity) throws Exception {
        ParameterLookup parameter = (ParameterLookup) entity;
        binder.readBean(parameter);
        boolean isEdit = parameter.id != null;
        Dialog dialog = new Dialog(isEdit ? "Edit Parameter" : "Add Parameter");

        FormLayout parameterForm = new FormLayout(key, value, description);
        dialog.add(parameterForm);

        Button saveBtn = new Button("Save", e -> {
            try {
                if (binder.writeBeanIfValid(parameter)) {
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
                    grid.getDataProvider().refreshAll();
                } else {
                    Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } catch (Exception ex) {
                Notification.show("Failed to save parameter: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", ev -> dialog.close());

        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

    @Override
    public void confirmDelete(Object entity) {
        ParameterLookup parameter = (ParameterLookup) entity;
        Dialog confirmDialog = new Dialog("Delete");
        confirmDialog.add(new Paragraph("Are you sure you want to delete this data?"));

        Button deleteBtn = new Button("Delete", e -> {
            try {
                parameterLookupService.delete(parameter.id);
                Notification.show("Parameter deleted successfully", 3000, Notification.Position.TOP_END)
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
