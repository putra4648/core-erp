package id.putra.core_erp.purchase_requisition.ui.views.inventory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.purchase_requisition.model.Inventory;
import id.putra.core_erp.purchase_requisition.service.InventoryService;
import id.putra.core_erp.shared.ui.MainLayout;
import jakarta.annotation.security.PermitAll;

@Route(value = "pr/inventory", layout = MainLayout.class)
@PageTitle("PR Inventory | CORE ERP")
@PermitAll
public class InventoryView extends VerticalLayout {

    private final InventoryService inventoryService;

    private final TextField searchField = new TextField();
    private final Grid<Inventory> grid = new Grid<>();
    private final List<Inventory> inventoryList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public InventoryView(InventoryService inventoryService) {
        this.inventoryService = inventoryService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 header = new H2("Inventory & Stock Tracking");
        header.addClassNames(LumoUtility.FontSize.XLARGE);

        searchField.setPlaceholder("Search PO or Part Number...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("280px");
        searchField.addValueChangeListener(e -> refreshGrid());

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshGrid());

        HorizontalLayout toolbar = new HorizontalLayout(searchField, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        configureGrid();

        add(header, toolbar, grid);
        refreshGrid();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(i -> i.getPoNumber() != null ? i.getPoNumber() : "-").setHeader("PO Number")
                .setAutoWidth(true);
        grid.addColumn(i -> i.getPartNumber() != null ? i.getPartNumber() : "-").setHeader("Part Number")
                .setAutoWidth(true);
        grid.addColumn(i -> i.getPartName() != null ? i.getPartName() : "-").setHeader("Part Name").setFlexGrow(2);
        grid.addColumn(
                i -> (i.getArea() != null ? i.getArea() : "-") + " / " + (i.getShelf() != null ? i.getShelf() : "-"))
                .setHeader("Area / Shelf").setFlexGrow(1);
        grid.addColumn(i -> i.getSupplier() != null ? i.getSupplier() : "-").setHeader("Supplier").setFlexGrow(1);
        grid.addColumn(i -> i.getQty() != null ? i.getQty().toString() : "0").setHeader("Stock Qty").setAutoWidth(true);
        grid.addColumn(i -> i.getDate() != null ? dateFormat.format(i.getDate()) : "-").setHeader("Last Inbound")
                .setAutoWidth(true);
    }

    private void refreshGrid() {
        try {
            Inventory params = new Inventory();

            List<Inventory> res = inventoryService.getAll(params);
            if (res != null) {
                inventoryList.clear();
                inventoryList.addAll(res);
                grid.setItems(inventoryList);
            }
        } catch (Exception ex) {
            Notification.show("Failed to load inventory: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
