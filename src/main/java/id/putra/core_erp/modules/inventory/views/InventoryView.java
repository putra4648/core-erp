package id.putra.core_erp.modules.inventory.views;

import java.time.format.DateTimeFormatter;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.modules.inventory.model.InventoryItem;
import id.putra.core_erp.modules.inventory.service.InventoryItemCoreService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

/**
 * Warehouse stock listing — the per-lot balances that putaway writes into.
 *
 * <p>Owned by the {@code inventory} module because it renders
 * {@link InventoryItem}. The {@code inventory} route previously pointed at
 * {@code procurement.views.InventoryView}, which showed a different model
 * ({@code procurement.model.Inventory}) that putaway and transactions never touch.
 */
@Route(value = "inventory", layout = MainLayout.class)
@PageTitle("Inventory Stock | CORE ERP")
@Authenticated
public class InventoryView extends ViewWithDialog {

	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final int PAGE_SIZE = 100;

	private final InventoryItemCoreService inventoryItemService;

	private final TextField searchField = new TextField();
	private final Grid<InventoryItem> grid = new Grid<>();

	public InventoryView(InventoryItemCoreService inventoryItemService) {
		super();
		this.inventoryItemService = inventoryItemService;

		addHeader();
		addToolbar();
		configureGrid();
	}

	@Override
	public void addHeader() {
		H2 header = new H2("Inventory & Stock Tracking");
		header.addClassNames(LumoUtility.FontSize.XLARGE, "m-0");
		add(header);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search SKU, lot or serial...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.setWidth("280px");
		searchField.setValueChangeMode(ValueChangeMode.LAZY);
		searchField.addValueChangeListener(e -> refreshData());

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, refreshBtn);
		toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
		add(toolbar);
	}

	@Override
	public void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(item -> item.product != null ? item.product.sku : "-")
				.setHeader("SKU").setAutoWidth(true);
		grid.addColumn(item -> item.name != null ? item.name : "-")
				.setHeader("Item Name").setFlexGrow(2);
		grid.addColumn(item -> item.lotNumber != null ? item.lotNumber : "-")
				.setHeader("Lot").setAutoWidth(true);
		grid.addColumn(item -> item.serialNumber != null ? item.serialNumber : "-")
				.setHeader("Serial").setAutoWidth(true);
		grid.addColumn(item -> item.quantity != null ? item.quantity : 0L)
				.setHeader("Qty").setAutoWidth(true);
		grid.addColumn(item -> item.reservedQty != null ? item.reservedQty : 0L)
				.setHeader("Reserved").setAutoWidth(true);
		grid.addColumn(item -> item.expiredDate != null
				? item.expiredDate.format(DATE_FMT)
				: "-").setHeader("Expiry").setAutoWidth(true);
		grid.addColumn(item -> item.status != null ? item.status : "-")
				.setHeader("Status").setAutoWidth(true);

		add(grid);
		refreshData();
	}

	@Override
	public void refreshData() {
		try {
			grid.setItems(inventoryItemService.findAll(new InventoryItem(), Page.ofSize(PAGE_SIZE)));
		} catch (Exception ex) {
			Notification.show("Failed to load inventory: " + ex.getMessage(), 3000,
					Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	@Override
	protected void buildAndShowDialog(@NonNull Object entity) {
		// Read-only listing: stock is created by putaway, not edited here.
	}

	@Override
	public void confirmDelete(Object entity) {
		// Read-only listing.
	}
}
