package id.putra.core_erp.modules.warehouse.views;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.warehouse.model.Rack;
import id.putra.core_erp.modules.warehouse.model.Warehouse;
import id.putra.core_erp.modules.warehouse.model.Zone;
import id.putra.core_erp.modules.warehouse.service.RackCoreService;
import id.putra.core_erp.modules.warehouse.service.WarehouseService;
import id.putra.core_erp.modules.warehouse.service.ZoneCoreService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "master/warehouses", layout = MainLayout.class)
@PageTitle("Warehouses")
@Authenticated
public class WarehouseView extends VerticalLayout {

	private final WarehouseService warehouseService;
	private final ZoneCoreService zoneService;
	private final RackCoreService rackService;

	private final TabSheet tabs = new TabSheet();

	private final Grid<Warehouse> gridWarehouse = new Grid<>();
	private final Grid<Zone> gridZone = new Grid<>();
	private final Grid<Rack> gridRack = new Grid<>();

	private final Binder<@NonNull Warehouse> warehouseBinder = new BeanValidationBinder<>(Warehouse.class);
	private final TextField warehouseName = new TextField("Name");
	private final TextField warehouseLocation = new TextField("Location");
	private final NumberField warehouseArea = new NumberField("Area (m²)");

	private final Binder<@NonNull Zone> zoneBinder = new BeanValidationBinder<>(Zone.class);
	private final TextField zoneCode = new TextField("Code");
	private final TextField zoneName = new TextField("Name");
	private final TextField zoneType = new TextField("Type");
	private final ComboBox<Warehouse> zoneWarehouse = new ComboBox<>("Warehouse");

	private final Binder<@NonNull Rack> rackBinder = new BeanValidationBinder<>(Rack.class);
	private final TextField rackName = new TextField("Rack Name");
	private final IntegerField rackRows = new IntegerField("Rows");
	private final IntegerField rackCols = new IntegerField("Columns");
	private final ComboBox<Zone> rackZone = new ComboBox<>("Zone");

	public WarehouseView(WarehouseService warehouseService, ZoneCoreService zoneService, RackCoreService rackService) {
		this.warehouseService = warehouseService;
		this.zoneService = zoneService;
		this.rackService = rackService;

		setSizeFull();
		setPadding(true);
		setSpacing(true);

		configureValidation();

		addHeader();
		initTab();

	}

	private void configureValidation() {
		warehouseBinder.bind(warehouseName, "name");
		warehouseBinder.bind(warehouseLocation, "location");
		warehouseBinder.bind(warehouseArea, "area");

		zoneBinder.bind(zoneCode, "code");
		zoneBinder.bind(zoneName, "name");
		zoneBinder.bind(zoneType, "type");

		rackBinder.bind(rackName, "name");
		rackBinder.bind(rackRows, "rows");
		rackBinder.bind(rackCols, "cols");

	}

	private void initTab() {
		this.tabs.setSizeFull();
		this.tabs.add("Warehouse", warehouseComponent());
		this.tabs.add("Zone", zoneComponent());
		this.tabs.add("Rack", rackComponent());

		add(tabs);
	}

	private void addHeader() {
		H2 title = new H2("Warehouse Management");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Manage physical facilities, zones, storage and racks.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	// --- Warehouses Panel ---
	private Component warehouseComponent() {
		TextField searchField = new TextField();
		searchField.setPlaceholder("Search warehouses...");
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.setClearButtonVisible(true);
		searchField.addValueChangeListener(e -> loadWarehouseData(searchField));

		Button addBtn = new Button("Add Warehouse", VaadinIcon.PLUS.create(),
				e -> openWarehouseDialog(new Warehouse()));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
			searchField.clear();
			loadWarehouseData(searchField);
		});

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, searchField);

		gridWarehouse.setSizeFull();
		gridWarehouse.addColumn(w -> w.id != null ? w.id.toString() : "").setHeader("ID").setAutoWidth(true);
		gridWarehouse.addColumn(w -> w.name != null ? w.name : "").setHeader("Name").setFlexGrow(1);
		gridWarehouse.addColumn(w -> w.location != null ? w.location : "").setHeader("Location/Address")
				.setFlexGrow(1);
		gridWarehouse.addColumn(w -> w.area != null ? w.area + " m²" : "-").setHeader("Area")
				.setAutoWidth(true);

		gridWarehouse.addComponentColumn(wh -> {
			Button edit = new Button(VaadinIcon.EDIT.create(), e -> openWarehouseDialog(wh));
			edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
			Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
				try {
					warehouseService.delete(wh.id);
					Notification.show("Warehouse deleted", 3000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				} catch (Exception ex) {
					Notification.show("Delete failed: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			});
			delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
			return new HorizontalLayout(edit, delete);
		}).setHeader("Actions").setAutoWidth(true);

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setPadding(false);
		layout.setSpacing(true);
		layout.add(toolbar, gridWarehouse);

		loadWarehouseData(searchField);

		return layout;
	}

	private void openWarehouseDialog(Warehouse warehouse) {
		Warehouse wh = warehouse != null ? warehouse : new Warehouse();
		warehouseBinder.readBean(wh);

		Dialog dialog = new Dialog(wh.id == null ? "Add Warehouse" : "Edit Warehouse");
		dialog.setWidth("500px");

		FormLayout form = new FormLayout(warehouseName, warehouseLocation, warehouseArea);
		dialog.add(form);

		Button save = new Button("Save", e -> {
			try {
				if (warehouseBinder.writeBeanIfValid(wh)) {
					if (wh.id == null) {
						warehouseService.save(wh);
						Notification.show("Warehouse created successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					} else {
						warehouseService.update(wh);
						Notification.show("Warehouse updated successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					}
					dialog.close();
					gridWarehouse.setItems(warehouseService.findAll(new Warehouse(), Page.ofSize(100)));
				} else {
					Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		dialog.getFooter().add(new Button("Cancel", ev -> dialog.close()), save);
		dialog.open();
	}

	// --- Zones Panel ---
	private Component zoneComponent() {
		TextField searchField = new TextField();
		searchField.setPlaceholder("Search zones...");
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());

		Zone param = new Zone();
		param.name = searchField.getValue();

		gridZone.setItems(zoneService.findAll(param, Page.ofSize(100)));

		Button addBtn = new Button("Add Zone", VaadinIcon.PLUS.create(), e -> openZoneDialog(new Zone()));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
			searchField.clear();
			loadZoneData(searchField);
		});

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, searchField);

		gridZone.setSizeFull();
		gridZone.addColumn(z -> z.code != null ? z.code : "").setHeader("Code").setAutoWidth(true);
		gridZone.addColumn(z -> z.name != null ? z.name : "").setHeader("Name").setFlexGrow(1);
		gridZone.addColumn(z -> z.type != null ? z.type : "").setHeader("Type").setAutoWidth(true);

		gridZone.addComponentColumn(zone -> {
			Button edit = new Button(VaadinIcon.EDIT.create(), e -> openZoneDialog(zone));
			edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
			Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
				try {
					zoneService.delete(zone.id);
					Notification.show("Warehouse deleted", 3000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				} catch (Exception ex) {
					Notification.show("Delete failed: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			});
			delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
			return new HorizontalLayout(edit, delete);
		}).setHeader("Actions").setAutoWidth(true);

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setPadding(false);
		layout.setSpacing(true);
		layout.add(toolbar, gridZone);

		loadZoneData(searchField);

		return layout;
	}

	private void openZoneDialog(Zone zone) {
		Zone z = zone != null ? zone : new Zone();
		zoneWarehouse.setItems(warehouseService.findAll(new Warehouse(), null));
		zoneWarehouse.setItemLabelGenerator(w -> w.name != null ? w.name : "");
		zoneBinder.readBean(z);

		Dialog dialog = new Dialog(z.id == null ? "Add Zone" : "Edit Zone");
		dialog.setWidth("500px");

		dialog.add(new FormLayout(zoneCode, zoneName, zoneType, zoneWarehouse));

		Button save = new Button("Save", e -> {
			try {
				if (zoneBinder.writeBeanIfValid(z)) {
					if (z.id == null) {
						zoneService.save(z);
						Notification.show("Zone created successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					} else {
						zoneService.update(z);
						Notification.show("Zone updated successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					}
					dialog.close();
					gridZone.setItems(zoneService.findAll(new Zone(), Page.ofSize(100)));
				} else {
					Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		dialog.getFooter().add(new Button("Cancel", ev -> dialog.close()), save);
		dialog.open();
	}

	// --- Racks Panel ---
	private Component rackComponent() {
		TextField searchField = new TextField();
		searchField.setPlaceholder("Search racks...");
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.addValueChangeListener(e -> loadRackData(searchField));

		Button addBtn = new Button("Add Rack", VaadinIcon.PLUS.create(), e -> openRackDialog(new Rack()));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
			searchField.clear();
			loadRackData(searchField);
		});

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, searchField);

		gridRack.setSizeFull();
		gridRack.addColumn(r -> r.name != null ? r.name : "").setHeader("Name").setFlexGrow(1);
		gridRack.addColumn(r -> r.rows != null ? r.rows.toString() : "").setHeader("Rows").setAutoWidth(true);
		gridRack.addColumn(r -> r.cols != null ? r.cols.toString() : "").setHeader("Columns").setAutoWidth(true);

		gridRack.addComponentColumn(rack -> {
			Button edit = new Button(VaadinIcon.EDIT.create(), e -> openRackDialog(rack));
			edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
			Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
				try {
					rackService.delete(rack.id);
					Notification.show("Warehouse deleted", 3000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				} catch (Exception ex) {
					Notification.show("Delete failed: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			});
			delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
			return new HorizontalLayout(edit, delete);
		}).setHeader("Actions").setAutoWidth(true);

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setPadding(false);
		layout.setSpacing(true);
		layout.add(toolbar, gridRack);

		loadRackData(searchField);

		return layout;
	}

	private void openRackDialog(Rack rack) {
		Rack r = rack != null ? rack : new Rack();
		rackZone.setItems(zoneService.findAll(new Zone(), null));
		rackZone.setItemLabelGenerator(w -> w.name != null ? w.name : "");
		rackBinder.readBean(r);

		Dialog dialog = new Dialog(r.id == null ? "Add Rack" : "Edit Rack");
		dialog.setWidth("500px");

		dialog.add(new FormLayout(rackName, rackRows, rackCols, rackZone));

		Button save = new Button("Save", e -> {
			try {
				if (rackBinder.writeBeanIfValid(r)) {
					if (r.id == null) {
						rackService.save(r);
						Notification.show("Rack created successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					} else {
						rackService.update(r);
						Notification.show("Rack updated successfully", 3000, Notification.Position.TOP_END)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					}
					dialog.close();
					gridRack.setItems(rackService.findAll(new Rack(), Page.ofSize(100)));
				} else {
					Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		dialog.getFooter().add(new Button("Cancel", ev -> dialog.close()), save);
		dialog.open();
	}

	private void refreshData(TextField searchField, Object entity) throws Exception {
		String searchTerm = searchField.getValue() != null ? searchField.getValue() : "";

		if (entity instanceof Warehouse parsedEntity) {
			parsedEntity.name = searchTerm;

			this.gridWarehouse.setItems(warehouseService.findAll(parsedEntity, Page.ofSize(100)));

		} else if (entity instanceof Zone parsedEntity) {
			parsedEntity.code = searchTerm;

			this.gridZone.setItems(zoneService.findAll(parsedEntity, Page.ofSize(100)));

		} else if (entity instanceof Rack parsedEntity) {
			parsedEntity.name = searchTerm;

			this.gridRack.setItems(rackService.findAll(parsedEntity, Page.ofSize(100)));
		} else {
			throw new Exception("Entity is not defined");
		}
	}

	private void loadWarehouseData(TextField searchField) {
		try {
			refreshData(searchField, new Warehouse());
		} catch (Exception ex) {
			Notification.show(ex.getMessage(), 4000, Notification.Position.TOP_END)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	private void loadRackData(TextField searchField) {
		try {
			refreshData(searchField, new Rack());
		} catch (Exception ex) {
			Notification.show(ex.getMessage(), 4000, Notification.Position.TOP_END)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	private void loadZoneData(TextField searchField) {
		try {
			refreshData(searchField, new Zone());
		} catch (Exception ex) {
			Notification.show(ex.getMessage(), 4000, Notification.Position.TOP_END)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
}
