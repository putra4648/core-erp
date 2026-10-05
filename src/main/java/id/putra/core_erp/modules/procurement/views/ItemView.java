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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.modules.procurement.model.Item;
import id.putra.core_erp.modules.procurement.service.ItemService;
import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.modules.supplier.service.SupplierService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.security.Authenticated;

@Route(value = "items", layout = MainLayout.class)
@PageTitle("Items Catalog | PR System")
@Authenticated
public class ItemView extends VerticalLayout {

	private final ItemService itemService;
	private final SupplierService supplierService;

	private final Grid<Item> grid = new Grid<>();
	private final TextField searchField = new TextField();
	private final ComboBox<String> statusFilter = new ComboBox<>();
	private final List<Item> itemList = new ArrayList<>();

	public ItemView(ItemService itemService, SupplierService supplierService) {
		this.itemService = itemService;
		this.supplierService = supplierService;

		setSizeFull();
		setPadding(true);
		setSpacing(true);

		H2 header = new H2("Item Catalog");
		header.addClassNames(LumoUtility.FontSize.XLARGE);

		HorizontalLayout toolbar = createToolbar();
		configureGrid();

		add(header, toolbar, grid);
		refreshGrid();
	}

	private HorizontalLayout createToolbar() {
		searchField.setPlaceholder("Search item by name / code...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.setWidth("300px");
		searchField.addValueChangeListener(e -> refreshGrid());

		statusFilter.setPlaceholder("Status");
		statusFilter.setItems("ACTIVE", "INACTIVE");
		statusFilter.setValue("ACTIVE");
		statusFilter.setWidth("140px");
		statusFilter.addValueChangeListener(e -> refreshGrid());

		Button addBtn = new Button("Add Item", VaadinIcon.PLUS.create(), e -> openItemDialog(null));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshGrid());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, statusFilter, addBtn, refreshBtn);
		toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
		return toolbar;
	}

	private void configureGrid() {
		grid.setSizeFull();

		grid.addColumn(i -> i.getName() != null ? i.getName() : "-").setHeader("Item Name").setFlexGrow(2);
		grid.addColumn(i -> i.getPrice() != null ? String.format("%,d", i.getPrice()) : "-").setHeader("Price")
				.setAutoWidth(true);
		grid.addColumn(i -> i.getUnit() != null ? i.getUnit() : "-").setHeader("UOM").setAutoWidth(true);

		grid.addComponentColumn(i -> {
			Span badge = new Span(i.getStatus() != null ? i.getStatus() : "ACTIVE");
			if ("ACTIVE".equalsIgnoreCase(i.getStatus()) || i.getStatus() == null) {
				badge.getElement().getThemeList().add("badge success");
			} else {
				badge.getElement().getThemeList().add("badge error");
			}
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		grid.addComponentColumn(i -> {
			Button editBtn = new Button("Edit", VaadinIcon.EDIT.create(), e -> openItemDialog(i));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
			return editBtn;
		}).setHeader("Actions").setAutoWidth(true);
	}

	private void refreshGrid() {
		try {
			String search = searchField.getValue() != null ? searchField.getValue() : "";
			String status = statusFilter.getValue() != null ? statusFilter.getValue() : "ACTIVE";

			List<Item> items = new ArrayList<>();
//            List<Item> items = itemService.getItems(search, status, "", 1, 100, "name", "asc");
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

	private void openItemDialog(Item editItem) {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle(editItem == null ? "Add Item" : "Edit Item");
		dialog.setWidth("600px");

		FormLayout form = new FormLayout();
		TextField nameField = new TextField("Item Name");
		nameField.setRequired(true);
		TextField priceField = new TextField("Estimated Price");
		TextField unitField = new TextField("Unit of Measure (UOM)");
		unitField.setValue("PCS");
		TextField glAccField = new TextField("GL Account");
		TextArea descField = new TextArea("Description");

		ComboBox<Supplier> supplierCombo = new ComboBox<>("Supplier");
		// TODO: add supplier

		if (editItem != null) {
			nameField.setValue(editItem.getName() != null ? editItem.getName() : "");
			priceField.setValue(editItem.getPrice() != null ? editItem.getPrice().toString() : "0");
			unitField.setValue(editItem.getUnit() != null ? editItem.getUnit() : "PCS");
			glAccField.setValue(editItem.getGlAcc() != null ? editItem.getGlAcc() : "");
			descField.setValue(editItem.getDescription() != null ? editItem.getDescription() : "");

		}

		form.add(nameField, supplierCombo, priceField, unitField, glAccField, descField);
		form.setColspan(descField, 2);

		Button saveBtn = new Button("Save", e -> {

			Item dto = new Item();
			dto.setName(nameField.getValue());

			try {
				dto.setPrice(new BigInteger(priceField.getValue().replaceAll("[^0-9]", "")));
			} catch (Exception ignored) {
				dto.setPrice(BigInteger.ZERO);
			}
			dto.setUnit(unitField.getValue());
			dto.setGlAcc(glAccField.getValue());
			dto.setDescription(descField.getValue());
			dto.setStatus("ACTIVE");

			try {
				if (editItem == null) {
					itemService.save(dto);
					Notification.show("Item added successfully", 3000, Notification.Position.TOP_CENTER)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				} else {
					itemService.update(dto);
					Notification.show("Item updated successfully", 3000, Notification.Position.TOP_CENTER)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				}
				dialog.close();
				refreshGrid();
			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button cancelBtn = new Button("Cancel", e -> dialog.close());

		dialog.add(form);
		dialog.getFooter().add(cancelBtn, saveBtn);
		dialog.open();
	}
}
