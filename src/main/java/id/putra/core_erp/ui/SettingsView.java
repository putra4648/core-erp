package id.putra.core_erp.ui;

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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.service.DepartmentService;
import id.putra.core_erp.modules.parameter.model.ParameterLookup;
import id.putra.core_erp.modules.parameter.service.ParameterLookupService;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "settings", layout = MainLayout.class)
@PageTitle("Settings")
@Authenticated
public class SettingsView extends VerticalLayout {

	private final ParameterLookupService parameterLookupService;
	private final DepartmentService departmentService;
	private final TabSheet tabs = new TabSheet();
	private final Grid<ParameterLookup> gridParameterLookup = new Grid<>();
	private final Grid<Department> gridDepartment = new Grid<>();

	private final Binder<ParameterLookup> parameterBinder = new BeanValidationBinder<>(ParameterLookup.class);
	private final TextField paramKey = new TextField("Key");
	private final TextField paramValue = new TextField("Value");
	private final TextField paramDescription = new TextField("Description");

	private final Binder<Department> departmentBinder = new BeanValidationBinder<>(Department.class);
	private final TextField deptCode = new TextField("Code");
	private final TextField deptName = new TextField("Name");

	public SettingsView(ParameterLookupService parameterLookupService, DepartmentService departmentService) {
		this.parameterLookupService = parameterLookupService;
		this.departmentService = departmentService;

		setSizeFull();
		setPadding(true);
		setSpacing(true);

		configureValidation();

		addHeader();
		initTab();
	}

	private void configureValidation() {
		parameterBinder.forField(paramKey)
				.asRequired("Key is required")
				.bind("key");
		parameterBinder.forField(paramValue)
				.asRequired("Value is required")
				.bind("value");
		parameterBinder.forField(paramDescription)
				.bind("description");

		departmentBinder.forField(deptCode)
				.asRequired("Code is required")
				.bind("code");
		departmentBinder.forField(deptName)
				.asRequired("Name is required")
				.bind("name");
	}

	private void initTab() {
		this.tabs.setSizeFull();
		this.tabs.add("System Parameter", parameterComponent());
		this.tabs.add("Department", departmentComponent());

		add(tabs);
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
			parsedEntity.key = searchTerm;

			this.gridParameterLookup.setItems(parameterLookupService.findAll(parsedEntity, Page.ofSize(100)));

		} else if (entity instanceof Department parsedEntity) {
			parsedEntity.name = searchTerm;

			this.gridDepartment.setItems(departmentService.findAll(parsedEntity, Page.ofSize(100)));
		} else {
			throw new Exception("Entity is not defined");
		}
	}

	private Component parameterComponent() {
		gridParameterLookup.setSizeFull();
		gridParameterLookup.addColumn(p -> p.key).setHeader("Key").setKey("key").setSortable(true).setAutoWidth(true);
		gridParameterLookup.addColumn(p -> p.value).setHeader("Value").setKey("value").setSortable(true)
				.setAutoWidth(true);
		gridParameterLookup.addColumn(p -> p.description).setHeader("Description").setKey("description")
				.setSortable(true).setFlexGrow(1);

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
		gridDepartment.addColumn(d -> d.code).setHeader("Code").setKey("code").setSortable(true).setAutoWidth(true);
		gridDepartment.addColumn(d -> d.name).setHeader("Name").setKey("name").setSortable(true).setAutoWidth(true);

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
		ParameterLookup p = parameter != null ? parameter : new ParameterLookup();
		parameterBinder.readBean(p);
		boolean isEdit = p.id != null;
		Dialog dialog = new Dialog(isEdit ? "Edit Parameter" : "Add Parameter");

		FormLayout parameterForm = new FormLayout(paramKey, paramValue, paramDescription);
		dialog.add(parameterForm);

		Button saveBtn = new Button("Save", e -> {
			try {
				if (parameterBinder.writeBeanIfValid(p)) {
					if (isEdit) {
						parameterLookupService.update(p);
						Notification.show("Parameter updated successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					} else {
						parameterLookupService.save(p);
						Notification.show("Parameter created successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					}
					dialog.close();
					gridParameterLookup.getDataProvider().refreshAll();
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

	private void openDepartmentDialog(Department dept) {
		Department d = dept != null ? dept : new Department();
		departmentBinder.readBean(d);
		boolean isEdit = d.id != null;
		Dialog dialog = new Dialog(isEdit ? "Edit Department" : "Add Department");

		FormLayout deptForm = new FormLayout(deptCode, deptName);
		dialog.add(deptForm);

		Button saveBtn = new Button("Save", e -> {
			try {
				if (departmentBinder.writeBeanIfValid(d)) {
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
					gridDepartment.getDataProvider().refreshAll();
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
