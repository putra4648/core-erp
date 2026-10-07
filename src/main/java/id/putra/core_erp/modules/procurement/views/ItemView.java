package id.putra.core_erp.modules.procurement.views;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.modules.procurement.model.Item;
import id.putra.core_erp.modules.procurement.service.ItemService;
import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.modules.supplier.service.SupplierService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.security.Authenticated;

@Route(value = "items", layout = MainLayout.class)
@PageTitle("Items Catalog | PR System")
@Authenticated
public class ItemView extends ViewWithDialog {

	private final ItemService itemService;

	private final Grid<Item> grid = new Grid<>();
	private final TextField searchField = new TextField();
	private final ComboBox<String> statusFilter = new ComboBox<>();
	private final List<Item> itemList = new ArrayList<>();

	private Binder<Item> binder = new BeanValidationBinder<>(Item.class);
	private TextField name = new TextField("Item Name");
	private TextField price = new TextField("Estimated Price");
	private TextField unit = new TextField("Unit of Measure (UOM)");
	private TextField glAcc = new TextField("GL Account");
	private TextArea description = new TextArea("Description");
	private ComboBox<Supplier> supplierCombo = new ComboBox<>("Supplier");

	public ItemView(ItemService itemService) {
		super();
		this.itemService = itemService;

		configureValidation();

		addHeader();
		addToolbar();
		configureGrid();

	}

	private void configureValidation() {
		binder.forField(name)
				.asRequired("Item name is required")
				.bind("name");

		binder.forField(price)
				.asRequired("Price is required")
				.withConverter(
						str -> {
							try {
								return str != null && !str.isBlank() ? new BigInteger(str.replaceAll("[^0-9]", "")) : BigInteger.ZERO;
							} catch (Exception e) {
								return BigInteger.ZERO;
							}
						},
						bigInt -> bigInt != null ? bigInt.toString() : "0",
						"Invalid Price")
				.bind("price");

		binder.forField(unit)
				.asRequired("Unit of measure is required")
				.bind("unit");

		binder.forField(glAcc)
				.bind("glAcc");

		binder.forField(description)
				.bind("description");
	}

	@Override
	public void addHeader() {
		H2 header = new H2("Item Catalog");
		header.addClassNames(LumoUtility.FontSize.XLARGE);
		add(header);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search item by name / code...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.setWidth("300px");
		searchField.addValueChangeListener(e -> refreshData());

		statusFilter.setPlaceholder("Status");
		statusFilter.setItems("ACTIVE", "INACTIVE");
		statusFilter.setValue("ACTIVE");
		statusFilter.setWidth("140px");
		statusFilter.addValueChangeListener(e -> refreshData());

		Button addBtn = new Button("Add Item", VaadinIcon.PLUS.create(), e -> openDialog(new Item()));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, statusFilter, addBtn, refreshBtn);
		toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
		add(toolbar);
	}

	@Override
	public void configureGrid() {
		grid.setSizeFull();

		grid.addColumn(i -> i.name != null ? i.name : "-").setHeader("Item Name").setFlexGrow(2);
		grid.addColumn(i -> i.price != null ? String.format("%,d", i.price) : "-").setHeader("Price")
				.setAutoWidth(true);
		grid.addColumn(i -> i.unit != null ? i.unit : "-").setHeader("UOM").setAutoWidth(true);

		grid.addComponentColumn(i -> {
			Span badge = new Span(i.status != null ? i.status : "ACTIVE");
			if ("ACTIVE".equalsIgnoreCase(i.status) || i.status == null) {
				badge.getElement().getThemeList().add("badge success");
			} else {
				badge.getElement().getThemeList().add("badge error");
			}
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		grid.addComponentColumn(i -> {
			Button editBtn = new Button("Edit", VaadinIcon.EDIT.create(), e -> openDialog(i));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
			return editBtn;
		}).setHeader("Actions").setAutoWidth(true);

		add(grid);

		refreshData();
	}

	@Override
	public void refreshData() {
		try {
			String search = searchField.getValue() != null ? searchField.getValue() : "";
			String status = statusFilter.getValue() != null ? statusFilter.getValue() : "ACTIVE";

			List<Item> items = new ArrayList<>();
			// List<Item> items = itemService.getItems(search, status, "", 1, 100, "name",
			// "asc");
			itemList.clear();
			if (items != null) {
				itemList.addAll(items);
			}
			grid.setItems(itemList);
		} catch (Exception ex) {
			Notification.show("Failed to load items: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	@Override
	public void openDialog(Object entity) {
		Item item = entity != null ? (Item) entity : new Item();
		if (item.unit == null || item.unit.isBlank()) {
			item.unit = "PCS";
		}
		if (item.status == null || item.status.isBlank()) {
			item.status = "ACTIVE";
		}
		binder.readBean(item);

		Dialog dialog = new Dialog();
		dialog.setHeaderTitle(item.id == null ? "Add Item" : "Edit Item");
		dialog.setWidth("600px");

		FormLayout form = new FormLayout();
		form.add(name, supplierCombo, price, unit, glAcc, description);
		form.setColspan(description, 2);

		Button saveBtn = new Button("Save", e -> {
			try {
				if (binder.writeBeanIfValid(item)) {
					if (item.id == null) {
						itemService.save(item);
						Notification.show("Item added successfully", 3000, Notification.Position.TOP_CENTER)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					} else {
						itemService.update(item);
						Notification.show("Item updated successfully", 3000, Notification.Position.TOP_CENTER)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					}
					dialog.close();
					refreshData();
				} else {
					Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_CENTER)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button cancelBtn = new Button("Cancel", ev -> dialog.close());

		dialog.add(form);
		dialog.getFooter().add(cancelBtn, saveBtn);
		dialog.open();
	}

	@Override
	public void confirmDelete(Object entity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'confirmDelete'");
	}
}
