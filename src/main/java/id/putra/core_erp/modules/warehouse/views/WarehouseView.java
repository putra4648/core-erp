package id.putra.core_erp.modules.warehouse.views;

import java.util.ArrayList;
import java.util.List;

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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.warehouse.model.Location;
import id.putra.core_erp.modules.warehouse.model.Rack;
import id.putra.core_erp.modules.warehouse.model.Warehouse;
import id.putra.core_erp.modules.warehouse.model.Zone;
import id.putra.core_erp.modules.warehouse.service.LocationCoreService;
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

	private final VerticalLayout contentArea = new VerticalLayout();
	private final LocationCoreService locationService;
	private final RackCoreService rackService;
	private final Tab tabLocations = new Tab("Locations");

	private final Tab tabRacks = new Tab("Racks");
	private final Tabs tabs = new Tabs();
	private final Tab tabWarehouses = new Tab("Warehouses");
	private final Tab tabZones = new Tab("Zones");
	private final WarehouseService warehouseService;
	private final ZoneCoreService zoneService;

	public WarehouseView(WarehouseService warehouseService, ZoneCoreService zoneService, RackCoreService rackService,
			LocationCoreService locationService) {
		this.warehouseService = warehouseService;
		this.zoneService = zoneService;
		this.rackService = rackService;
		this.locationService = locationService;

		setSizeFull();
		setPadding(true);
		setSpacing(true);

		addHeader();
		configureTabs();

		contentArea.setSizeFull();
		contentArea.setPadding(false);
		add(tabs, contentArea);

		showWarehousesPanel();
	}

	private void addHeader() {
		H2 title = new H2("Warehouse Management");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Manage physical facilities, zones, storage racks, and location bin numbers.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	private void configureTabs() {
		tabs.add(tabWarehouses, tabZones, tabRacks, tabLocations);
		tabs.addSelectedChangeListener(event -> {
			contentArea.removeAll();
			Tab selected = tabs.getSelectedTab();
			if (selected.equals(tabWarehouses)) {
				showWarehousesPanel();
			} else if (selected.equals(tabZones)) {
				showZonesPanel();
			} else if (selected.equals(tabRacks)) {
				showRacksPanel();
			} else if (selected.equals(tabLocations)) {
				showLocationsPanel();
			}
		});
	}

	private void openLocationDialog(Location location, Runnable onSave) {
		Dialog dialog = new Dialog("Add Location");
		TextField name = new TextField("Location Name");
		TextField bin = new TextField("Bin Number");
		TextField type = new TextField("Location Type");
		dialog.add(new FormLayout(name, bin, type));

		Button save = new Button("Save", e -> {
			Location dto = new Location();
			dto.setName(name.getValue());
			dto.setBinNumber(bin.getValue());
			dto.setType(type.getValue());

			locationService.save(dto);
			dialog.close();
			onSave.run();
		});
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		dialog.getFooter().add(new Button("Cancel", e -> dialog.close()), save);
		dialog.open();
	}

	private void openRackDialog(Rack rack, Runnable onSave) {
		Dialog dialog = new Dialog("Add Rack");
		TextField name = new TextField("Rack Name");
		NumberField rows = new NumberField("Rows");
		NumberField cols = new NumberField("Columns");
		dialog.add(new FormLayout(name, rows, cols));

		Button save = new Button("Save", e -> {
			Rack dto = new Rack();
			dto.setName(name.getValue());
			dto.setRows(rows.getValue() != null ? rows.getValue().longValue() : 1L);
			dto.setCols(cols.getValue() != null ? cols.getValue().longValue() : 1L);
			rackService.save(dto);
			dialog.close();
			onSave.run();
		});
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		dialog.getFooter().add(new Button("Cancel", e -> dialog.close()), save);
		dialog.open();
	}

	private void openWarehouseDialog(Warehouse warehouse, Runnable onSave) {
		boolean isEdit = warehouse.getId() != null;
		Dialog dialog = new Dialog(isEdit ? "Edit Warehouse" : "Add Warehouse");

		TextField nameField = new TextField("Name");
		TextField locationField = new TextField("Location");
		NumberField areaField = new NumberField("Area (m²)");

		nameField.setValue(warehouse.getName() != null ? warehouse.getName() : "");
		locationField.setValue(warehouse.getLocation() != null ? warehouse.getLocation() : "");
		areaField.setValue(warehouse.getArea() != null ? warehouse.getArea() : 0.0);

		FormLayout form = new FormLayout(nameField, locationField, areaField);
		dialog.add(form);

		Button save = new Button("Save", e -> {
			Warehouse dto = new Warehouse();
			dto.setId(warehouse.getId());
			dto.setName(nameField.getValue());
			dto.setLocation(locationField.getValue());
			dto.setArea(areaField.getValue());

			try {
				if (isEdit) {
					warehouseService.update(dto);
				} else {
					warehouseService.save(dto);
				}
				Notification.show("Warehouse saved", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				dialog.close();
				onSave.run();
			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		dialog.getFooter().add(new Button("Cancel", e -> dialog.close()), save);
		dialog.open();
	}

	private void openZoneDialog(Zone zone, Runnable onSave) {
		Dialog dialog = new Dialog("Add Zone");
		TextField code = new TextField("Zone Code");
		TextField name = new TextField("Zone Name");
		TextField type = new TextField("Zone Type");
		dialog.add(new FormLayout(code, name, type));

		Button save = new Button("Save", e -> {
			Zone dto = new Zone();
			dto.setCode(code.getValue());
			dto.setName(name.getValue());
			dto.setType(type.getValue());
			zoneService.save(dto);
			dialog.close();
			onSave.run();
		});
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		dialog.getFooter().add(new Button("Cancel", e -> dialog.close()), save);
		dialog.open();
	}

	// --- Locations Panel ---
	private void showLocationsPanel() {
		Grid<Location> grid = new Grid<>();
		TextField search = new TextField();
		search.setPlaceholder("Search locations / bin numbers...");
		search.setPrefixComponent(VaadinIcon.SEARCH.create());

		Runnable refresh = () -> {
			Location param = new Location();
			param.setName(search.getValue());
			List<Location> res = new ArrayList<>();
			locationService.findAll(param, Page.ofSize(100)).forEach(res::add);
			grid.setItems(res);
		};
		search.addValueChangeListener(e -> refresh.run());

		Button addBtn = new Button("Add Location", VaadinIcon.PLUS.create(),
				e -> openLocationDialog(new Location(), refresh));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		HorizontalLayout toolbar = new HorizontalLayout(search, addBtn);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, search);

		grid.setSizeFull();
		grid.addColumn(l -> l.getName() != null ? l.getName() : "").setHeader("Location Name").setFlexGrow(1);
		grid.addColumn(l -> l.getBinNumber() != null ? l.getBinNumber() : "").setHeader("Bin Number")
				.setAutoWidth(true);
		grid.addColumn(l -> l.getType() != null ? l.getType() : "").setHeader("Type").setAutoWidth(true);

		contentArea.add(toolbar, grid);
		refresh.run();
	}

	// --- Racks Panel ---
	private void showRacksPanel() {
		Grid<Rack> grid = new Grid<>();
		TextField search = new TextField();
		search.setPlaceholder("Search racks...");
		search.setPrefixComponent(VaadinIcon.SEARCH.create());

		Runnable refresh = () -> {
			Rack param = new Rack();
			param.setName(search.getValue());
			List<Rack> res = new ArrayList<>();
			rackService.findAll(param, Page.ofSize(100)).forEach(res::add);
			grid.setItems(res);
		};
		search.addValueChangeListener(e -> refresh.run());

		Button addBtn = new Button("Add Rack", VaadinIcon.PLUS.create(), e -> openRackDialog(new Rack(), refresh));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		HorizontalLayout toolbar = new HorizontalLayout(search, addBtn);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, search);

		grid.setSizeFull();
		grid.addColumn(r -> r.getName() != null ? r.getName() : "").setHeader("Rack Name").setFlexGrow(1);
		grid.addColumn(r -> r.getRows() != null ? r.getRows().toString() : "").setHeader("Rows").setAutoWidth(true);
		grid.addColumn(r -> r.getCols() != null ? r.getCols().toString() : "").setHeader("Columns").setAutoWidth(true);

		contentArea.add(toolbar, grid);
		refresh.run();
	}

	// --- Warehouses Panel ---
	private void showWarehousesPanel() {
		Grid<Warehouse> grid = new Grid<>();
		TextField search = new TextField();
		search.setPlaceholder("Search warehouses...");
		search.setPrefixComponent(VaadinIcon.SEARCH.create());
		search.setClearButtonVisible(true);

		Runnable refresh = () -> {
			Warehouse param = new Warehouse();
			param.setName(search.getValue());
			List<Warehouse> res = new ArrayList<>();
			warehouseService.findAll(param, Page.ofSize(100)).forEach(res::add);
			grid.setItems(res);
		};
		search.addValueChangeListener(e -> refresh.run());

		Button addBtn = new Button("Add Warehouse", VaadinIcon.PLUS.create(),
				e -> openWarehouseDialog(new Warehouse(), refresh));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		HorizontalLayout toolbar = new HorizontalLayout(search, addBtn);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, search);

		grid.setSizeFull();
		grid.addColumn(w -> w.getId() != null ? w.getId().toString() : "").setHeader("ID").setAutoWidth(true);
		grid.addColumn(w -> w.getName() != null ? w.getName() : "").setHeader("Warehouse Name").setFlexGrow(1);
		grid.addColumn(w -> w.getLocation() != null ? w.getLocation() : "").setHeader("Location/Address")
				.setFlexGrow(1);
		grid.addColumn(w -> w.getArea() != null ? w.getArea() + " m²" : "-").setHeader("Area").setAutoWidth(true);

		grid.addComponentColumn(wh -> {
			Button edit = new Button(VaadinIcon.EDIT.create(), e -> openWarehouseDialog(wh, refresh));
			edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
			Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
				try {
					warehouseService.delete(wh.getId());
					Notification.show("Warehouse deleted", 3000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					refresh.run();
				} catch (Exception ex) {
					Notification.show("Delete failed: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			});
			delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
			return new HorizontalLayout(edit, delete);
		}).setHeader("Actions").setAutoWidth(true);

		contentArea.add(toolbar, grid);
		refresh.run();
	}

	// --- Zones Panel ---
	private void showZonesPanel() {
		Grid<Zone> grid = new Grid<>();
		TextField search = new TextField();
		search.setPlaceholder("Search zones...");
		search.setPrefixComponent(VaadinIcon.SEARCH.create());

		Runnable refresh = () -> {
			Zone param = new Zone();
			param.setName(search.getValue());
			List<Zone> res = new ArrayList<>();
			zoneService.findAll(param, Page.ofSize(100)).forEach(res::add);
			grid.setItems(res);
		};
		search.addValueChangeListener(e -> refresh.run());

		Button addBtn = new Button("Add Zone", VaadinIcon.PLUS.create(), e -> openZoneDialog(new Zone(), refresh));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		HorizontalLayout toolbar = new HorizontalLayout(search, addBtn);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, search);

		grid.setSizeFull();
		grid.addColumn(z -> z.getCode() != null ? z.getCode() : "").setHeader("Zone Code").setAutoWidth(true);
		grid.addColumn(z -> z.getName() != null ? z.getName() : "").setHeader("Zone Name").setFlexGrow(1);
		grid.addColumn(z -> z.getType() != null ? z.getType() : "").setHeader("Type").setAutoWidth(true);

		grid.addComponentColumn(z -> {
			Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
				zoneService.delete(z.getId());
				refresh.run();
			});
			delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
			return delete;
		}).setHeader("Actions").setAutoWidth(true);

		contentArea.add(toolbar, grid);
		refresh.run();
	}
}
