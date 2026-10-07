package id.putra.core_erp.modules.customer.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.customer.model.Customer;
import id.putra.core_erp.modules.customer.service.CustomerService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.panache.common.Page;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.security.Authenticated;

@Route(value = "master/customer", layout = MainLayout.class)
@PageTitle("Customers")
@Authenticated
public class CustomerView extends ViewWithDialog {

	private final CustomerService customerService;
	private final Grid<Customer> grid = new Grid<>();
	private final TextField searchField = new TextField();

	private Binder<Customer> binder = new BeanValidationBinder<>(Customer.class);
	private TextField name = new TextField("Customer Name");
	private TextField email = new TextField("Email");
	private TextField phone = new TextField("Phone");

	public CustomerView(CustomerService customerService) {
		super();
		this.customerService = customerService;

		configureValidation();

		addHeader();
		addToolbar();
		configureGrid();

		refreshData();
	}

	private void configureValidation() {
		binder.forField(name)
				.asRequired("Customer name is required")
				.bind("name");

		binder.forField(email)
				.bind("email");

		binder.forField(phone)
				.bind("phone");
	}

	@Override
	public void addHeader() {
		H2 title = new H2("Customer Management");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Manage customer details and contact information.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search customers...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.addValueChangeListener(e -> refreshData());

		Button addButton = new Button("Add Customer", VaadinIcon.PLUS.create(),
				e -> openDialog(new Customer()));
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
		grid.addColumn(c -> c.name != null ? c.name : "").setHeader("Name").setFlexGrow(1).setSortable(true);
		grid.addColumn(c -> c.email != null ? c.email : "-").setHeader("Email").setFlexGrow(1);
		grid.addColumn(c -> c.phone != null ? c.phone : "-").setHeader("Phone").setAutoWidth(true);

		grid.addComponentColumn(customer ->

		{
			Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDialog(customer));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(customer));
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

			return new HorizontalLayout(editBtn, deleteBtn);
		}).setHeader("Actions").setAutoWidth(true);

		add(grid);

		refreshData();
	}

	@Override
	public void refreshData() {
		var params = new Customer();
		params.name = (searchField.getValue());
		grid.setItems(customerService.findAll(params, Page.ofSize(100)));
	}

	@Override
	public void openDialog(Object entity) {
		Customer customer = entity != null ? (Customer) entity : new Customer();
		binder.readBean(customer);

		Dialog dialog = new Dialog(customer.id == null ? "Add Customer" : "Edit Customer");
		dialog.setWidth("500px");

		FormLayout formLayout = new FormLayout(name, email, phone);
		dialog.add(formLayout);

		Button saveBtn = new Button("Save", e -> {
			try {
				if (binder.writeBeanIfValid(customer)) {
					if (customer.id != null) {
						customerService.update(customer);
						Notification.show("Customer updated successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					} else {
						customerService.save(customer);
						Notification.show("Customer created successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					}
					dialog.close();
					refreshData();
				} else {
					Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			} catch (Exception ex) {
				Notification.show("Failed to save customer: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
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
		Customer customer = (Customer) entity;
		Dialog confirmDialog = new Dialog("Delete Customer");
		confirmDialog.add(new Paragraph("Are you sure you want to delete customer '" + customer.name + "'?"));

		Button deleteBtn = new Button("Delete", e -> {
			try {
				customerService.delete(customer.id);
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
