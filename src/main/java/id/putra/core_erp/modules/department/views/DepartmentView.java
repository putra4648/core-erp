package id.putra.core_erp.modules.department.views;

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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.service.DepartmentService;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route("department")
@PageTitle(value = "Master Department")
@Authenticated
public class DepartmentView extends ViewWithDialog {

	private final DepartmentService departmentService;
	private final Grid<Department> grid = new Grid<>();
	private final TextField searchField = new TextField();

	private Binder<Department> binder = new BeanValidationBinder<>(Department.class);
	private TextField code = new TextField("Department Code");
	private TextField name = new TextField("Department Name");

	public DepartmentView(DepartmentService departmentService) {
		super();
		this.departmentService = departmentService;

		configureValidation();

		addHeader();
		addToolbar();
		configureGrid();
		add(grid);

	}

	private void configureValidation() {
		binder.bind(code, "code");
		binder.bind(name, "name");
	}

	@Override
	public void addHeader() {
		H2 title = new H2("Department Management");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Manage departments and contact information.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search departments...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.addValueChangeListener(e -> refreshData());

		Button addButton = new Button("Add Department", VaadinIcon.PLUS.create(),
				e -> openDialog(new Department()));
		addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshButton = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton, refreshButton);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, searchField);

		add(toolbar);
	}

	@Override
	public void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(dept -> dept.code != null ? dept.code : "").setHeader("Code").setAutoWidth(true)
				.setSortable(true);
		grid.addColumn(dept -> dept.name != null ? dept.name : "").setHeader("Name").setAutoWidth(true)
				.setSortable(true);

		grid.addComponentColumn(department -> {
			Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDialog(department));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(department));
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

			return new HorizontalLayout(editBtn, deleteBtn);
		}).setHeader("Actions").setAutoWidth(true);

		refreshData();

		add(grid);

	}

	@Override
	public void refreshData() {
		var dept = new Department();
		dept.name = searchField.getValue();
		grid.setItems(departmentService.findAll(dept, Page.ofSize(100)));
	}

	@Override
	protected void buildAndShowDialog(Object entity) throws Exception {
		Department dept = entity != null ? (Department) entity : new Department();
		binder.readBean(dept);

		Dialog dialog = new Dialog(dept.id == null ? "Add Department" : "Edit Department");
		dialog.setWidth("500px");

		FormLayout formLayout = new FormLayout(code, name);
		dialog.add(formLayout);

		Button saveBtn = new Button("Save", e -> {
			try {
				if (binder.writeBeanIfValid(dept)) {
					if (dept.id != null) {
						departmentService.update(dept);
						Notification.show("Department updated successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					} else {
						departmentService.save(dept);
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
				Notification.show("Failed to save department: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button cancelBtn = new Button("Cancel", e -> dialog.close());

		dialog.getFooter().add(cancelBtn, saveBtn);
		dialog.open();
	}

	@Override
	public void confirmDelete(Object entity) {
		Department department = (Department) entity;
		Dialog confirmDialog = new Dialog("Delete Customer");
		confirmDialog.add(new Paragraph("Are you sure you want to delete customer '" + department.name + "'?"));

		Button deleteBtn = new Button("Delete", e -> {
			try {
				departmentService.delete(department.id);
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
