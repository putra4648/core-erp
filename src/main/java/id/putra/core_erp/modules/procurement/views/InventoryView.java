package id.putra.core_erp.modules.procurement.views;

import java.text.SimpleDateFormat;

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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.modules.procurement.model.Inventory;
import id.putra.core_erp.modules.procurement.service.InventoryService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "inventory", layout = MainLayout.class)
@PageTitle("PR Inventory | CORE ERP")
@Authenticated
public class InventoryView extends ViewWithDialog {

	private final InventoryService inventoryService;

	private final TextField searchField = new TextField();
	private final Grid<Inventory> grid = new Grid<>();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public InventoryView(InventoryService inventoryService) {
		super();
		this.inventoryService = inventoryService;

		addHeader();
		addToolbar();
		configureGrid();

	}

	@Override
	public void addHeader() {
		H2 header = new H2("Inventory & Stock Tracking");
		header.addClassNames(LumoUtility.FontSize.XLARGE);
		add(header);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search PO or Part Number...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.setWidth("280px");
		searchField.addValueChangeListener(e -> refreshData());

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, refreshBtn);
		toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
	}

	@Override
	public void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(i -> i.name != null ? i.name : "-").setHeader("Part Name").setFlexGrow(2);
		grid.addColumn(i -> i.qty != null ? i.qty.toString() : "0").setHeader("Stock Qty").setAutoWidth(true);
		grid.addColumn(i -> i.date != null ? dateFormat.format(i.date) : "-").setHeader("Last Inbound")
				.setAutoWidth(true);

		add(grid);

		refreshData();
	}

	@Override
	public void refreshData() {
		try {
			Inventory params = new Inventory();
			grid.setItems(inventoryService.findAll(params, Page.ofSize(100)));
		} catch (Exception ex) {
			Notification.show("Failed to load inventory: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	@Override
	protected void buildAndShowDialog(@NonNull Object entity) throws Exception {
	}

	@Override
	public void confirmDelete(Object entity) {
	}
}
