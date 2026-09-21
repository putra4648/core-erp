package id.putra.core_erp.shared.ui;

import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.parameter.model.ParameterLookup;
import id.putra.core_erp.modules.parameter.service.ParameterLookupService;
import io.quarkus.panache.common.Page;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Route(value = "settings", layout = MainLayout.class)
@PageTitle("Settings | WMS Pro")
@PermitAll
public class SettingsView extends DefaultComponent {

    @Inject
    Validator validator;

    private final Tab parameterTabs = new Tab(VaadinIcon.COG.create(), new Span("System Parameters"));
    private final Tabs tabs = new Tabs(this.parameterTabs);
    private final VerticalLayout contentArea;
    private final Grid<ParameterLookup> parameterGrid = new Grid<>();
    private final TextField searchField;
    private ParameterLookup form;

    private final ParameterLookupService parameterLookupService;

    public SettingsView(ParameterLookupService parameterLookupService) {
        this.form = new ParameterLookup();
        this.contentArea = new VerticalLayout();
        this.parameterLookupService = parameterLookupService;
        this.searchField = new TextField();

        super("Parameter Lookup");

        VerticalLayout formLayout = new VerticalLayout();
        var keyField = new TextField("Key", val -> {
            this.form.setKey(val.getValue());
        });
        keyField.setRequired(true);

        var valueField = new TextField("Value", val -> {
            this.form.setValue(val.getValue());
        });
        valueField.setRequired(true);

        var descriptionField = new TextField("Description", val -> {
            this.form.setDescription(val.getValue());
        });
        descriptionField.setRequired(true);

        this.parameterGrid.setItems(query -> parameterLookupService
                .getAllParameterLookups(searchField.getValue(), Page.ofSize(query.getPageSize())).stream());

        formLayout.add(keyField, valueField, descriptionField);

        this.addFormComponent(formLayout);

        addHeader();

        tabs.addSelectedChangeListener(e -> updateContent(e.getSelectedTab()));
        contentArea.setSizeFull();
        contentArea.setPadding(false);

        add(tabs, contentArea);

        configureParameterGrid();

        updateContent(parameterTabs);

    }

    private void updateContent(Tab selectedTab) {
        contentArea.removeAll();
        if (selectedTab.equals(parameterTabs)) {
            configureParameterSection();
            this.parameterGrid.getDataProvider().refreshAll();
        }
    }

    private void configureParameterGrid() {
        parameterGrid.addColumn(p -> p.getKey()).setHeader("Key").setKey("key").setSortable(true);
        parameterGrid.addColumn(p -> p.getValue()).setHeader("Value").setKey("value").setSortable(true);
        parameterGrid.addColumn(p -> p.getDescription()).setHeader("Description").setKey("description")
                .setSortable(true);
    }

    private void configureParameterSection() {
        this.searchField.setPlaceholder("Search parameters...");
        this.searchField.setClearButtonVisible(true);
        this.searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        this.searchField.setWidth("280px");
        this.searchField.setValueChangeMode(ValueChangeMode.LAZY);
        this.searchField.addValueChangeListener(e -> this.parameterGrid.getDataProvider().refreshAll());

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create(), e -> openAddParameterDialog());
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
            this.searchField.clear();
            this.parameterGrid.getDataProvider().refreshAll();
        });

        HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(toolbar, parameterGrid);

        contentArea.add(layout);

    }

    private void openAddParameterDialog() {
        this.openDialog();
    }

    private void addHeader() {
        H2 title = new H2("System Settings");
        title.addClassNames("m-0", "text-xl", "font-bold");
        Paragraph desc = new Paragraph("Configure application parameters, dark mode, and system defaults.");
        desc.addClassNames("text-secondary", "m-0");
        add(title, desc);
    }

    @Override
    public void saveAction() {
        Set<ConstraintViolation<ParameterLookup>> validate = validator.validate(form);
        if (validate.isEmpty()) {
            try {
                parameterLookupService.persist(List.of(form));
                this.parameterGrid.getDataProvider().refreshAll();
                this.closeDialog();
                Notification
                        .show("Parameter saved successfully", 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (Exception ex) {
                Notification
                        .show("Failed to save parameter: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }

    @Override
    public void closeAction() {
        this.closeDialog();
    }
}
