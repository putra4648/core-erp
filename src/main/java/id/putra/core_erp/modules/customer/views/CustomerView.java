package id.putra.core_erp.modules.customer.views;

import java.util.ArrayList;
import java.util.List;

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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.customer.model.Customer;
import id.putra.core_erp.modules.customer.service.CustomerService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "master/customer", layout = MainLayout.class)
@PageTitle("Customers")
@Authenticated
public class CustomerView extends VerticalLayout {

	private final CustomerService customerService;
	private final Grid<Customer> grid = new Grid<>();
	private final TextField searchField = new TextField();

	public CustomerView(CustomerService customerService) {
		this.customerService = customerService;

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
				e -> openCustomerDialog(new Customer()));
		addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshButton = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton, refreshButton);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, searchField);
		add(toolbar);
	}

	private void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(c -> c.getName() != null ? c.getName() : "").setHeader("Name").setFlexGrow(1).setSortable(true);
		grid.addColumn(c -> c.getEmail() != null ? c.getEmail() : "-").setHeader("Email").setFlexGrow(1);
		grid.addColumn(c -> c.getPhone() != null ? c.getPhone() : "-").setHeader("Phone").setAutoWidth(true);

		grid.addComponentColumn(customer ->

		{
			Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openCustomerDialog(customer));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(customer));
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

			return new HorizontalLayout(editBtn, deleteBtn);
		}).setHeader("Actions").setAutoWidth(true);
	}

	private void refreshData() {
		var params = new Customer();
		params.setName(searchField.getValue());
		List<Customer> res = new ArrayList<>();
		customerService.findAll(params, Page.ofSize(100)).forEach(res::add);
		grid.setItems(res);
	}

	private void openCustomerDialog(Customer customer) {
		Dialog dialog = new Dialog("Customer");

		TextField nameField = new TextField("Customer Name");
		Checkbox activeCheck = new Checkbox("Is Active");
		TextField emailField = new TextField("Email");
		TextField phoneField = new TextField("Phone");

		nameField.setRequired(true);
		nameField.setValue(customer.getName() != null ? customer.getName() : "");
		emailField.setValue(customer.getEmail() != null ? customer.getEmail() : "");
		phoneField.setValue(customer.getPhone() != null ? customer.getPhone() : "");
		activeCheck.setValue(customer.getIsActive() != null ? customer.getIsActive() : true);

		FormLayout formLayout = new FormLayout(nameField, activeCheck, emailField, phoneField);
		dialog.add(formLayout);

		Button saveBtn = new Button("Save", e -> {
			try {
				var entity = new Customer();
				entity.id = customer.id;
				entity.setName(nameField.getValue());
				entity.setPhone(phoneField.getValue());
				entity.setEmail(emailField.getValue());
				entity.setIsActive(activeCheck.getValue());

				if (customer.id != null) {
					customerService.update(entity);
					Notification.show("Customer updated successfully", 3000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				} else {
					customerService.save(entity);
					Notification.show("Customer created successfully", 3000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				}
				dialog.close();
				refreshData();
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

	private void confirmDelete(Customer customer) {
		Dialog confirmDialog = new Dialog("Delete Customer");
		confirmDialog.add(new Paragraph("Are you sure you want to delete customer '" + customer.getName() + "'?"));

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
