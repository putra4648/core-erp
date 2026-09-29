package id.putra.core_erp.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

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

	private final VerticalLayout contentArea = new VerticalLayout();

	private final Grid<ParameterLookup> parameterGrid = new Grid<>();
	private final ParameterLookupService parameterLookupService;
	private final Tab parameterTabs = new Tab(VaadinIcon.COG.create(), new Span("System Parameters"));
	private final TextField searchField = new TextField();
	private final Tabs tabs = new Tabs(this.parameterTabs);
	@Inject
	Validator validator;

	public SettingsView(ParameterLookupService parameterLookupService) {
		this.parameterLookupService = parameterLookupService;

		setSizeFull();
		setPadding(true);
		setSpacing(true);

		addHeader();

		tabs.addSelectedChangeListener(e -> updateContent(e.getSelectedTab()));
		contentArea.setSizeFull();
		contentArea.setPadding(false);

		add(tabs, contentArea);

		configureParameterGrid();
		updateContent(parameterTabs);
	}

	private void addHeader() {
		H2 title = new H2("System Settings");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Configure application parameters, dark mode, and system defaults.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	private void configureParameterGrid() {
		parameterGrid.setSizeFull();
		parameterGrid.addColumn(ParameterLookup::getKey).setHeader("Key").setKey("key").setSortable(true)
				.setAutoWidth(true);
		parameterGrid.addColumn(ParameterLookup::getValue).setHeader("Value").setKey("value").setSortable(true)
				.setAutoWidth(true);
		parameterGrid.addColumn(ParameterLookup::getDescription).setHeader("Description").setKey("description")
				.setSortable(true).setFlexGrow(1);

		parameterGrid.addComponentColumn(param -> {
			Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openParameterDialog(param));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(param));
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

			return new HorizontalLayout(editBtn, deleteBtn);
		}).setHeader("Actions").setAutoWidth(true);
	}

	private void configureParameterSection() {
		searchField.setPlaceholder("Search parameters...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.setWidth("280px");
		searchField.addValueChangeListener(e -> refreshData());

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setPadding(false);
		layout.setSpacing(true);

		Button addBtn = new Button("Add Parameter", VaadinIcon.PLUS.create(),
				e -> openParameterDialog(new ParameterLookup()));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
			searchField.clear();
			refreshData();
		});

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
		toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

		layout.add(toolbar, parameterGrid);
		contentArea.add(layout);
	}

	private void confirmDelete(ParameterLookup parameter) {
		Dialog confirmDialog = new Dialog("Delete Parameter");
		confirmDialog.add(new Paragraph("Are you sure you want to delete parameter '" + parameter.getKey() + "'?"));

		Button deleteBtn = new Button("Delete", e -> {
			try {
				parameterLookupService.delete(parameter.getId());
				Notification.show("Parameter deleted successfully", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				confirmDialog.close();
				refreshData();
			} catch (Exception ex) {
				Notification.show("Failed to delete parameter: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

		Button cancelBtn = new Button("Cancel", e -> confirmDialog.close());

		confirmDialog.getFooter().add(cancelBtn, deleteBtn);
		confirmDialog.open();
	}

	private void openParameterDialog(ParameterLookup parameter) {
		boolean isEdit = parameter.getId() != null;
		Dialog dialog = new Dialog(isEdit ? "Edit Parameter" : "Add Parameter");

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
					if (sb.length() > 0) {
						sb.append(", ");
					}
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
				refreshData();
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

	private void refreshData() {
		ParameterLookup search = new ParameterLookup();
		String searchTerm = searchField.getValue();
		if (searchTerm != null && !searchTerm.isBlank()) {
			search.setKey(searchTerm.trim());
		}

		List<ParameterLookup> result = new ArrayList<>();
		parameterLookupService.findAll(search, Page.ofSize(100)).forEach(result::add);
		parameterGrid.setItems(result);
	}

	private void updateContent(Tab selectedTab) {
		contentArea.removeAll();
		if (selectedTab.equals(parameterTabs)) {
			configureParameterSection();
			refreshData();
		}
	}
}
