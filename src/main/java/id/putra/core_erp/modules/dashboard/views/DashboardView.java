package id.putra.core_erp.modules.dashboard.views;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.modules.customer.service.CustomerService;
import id.putra.core_erp.modules.inbound.service.PurchaseOrderService;
import id.putra.core_erp.modules.outbound.service.SalesOrderService;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.service.DashboardService;
import id.putra.core_erp.modules.product.service.ProductService;
import id.putra.core_erp.modules.supplier.service.SupplierService;
import id.putra.core_erp.modules.warehouse.service.WarehouseService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.security.Authenticated;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard")
@Authenticated
public class DashboardView extends VerticalLayout {

	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private final DashboardService dashboardService;
	private final ProductService productService;
	private final WarehouseService warehouseService;
	private final SupplierService supplierService;
	private final CustomerService customerService;
	private final PurchaseOrderService purchaseOrderService;
	private final SalesOrderService salesOrderService;

	private final Grid<Requisition> recentGrid = new Grid<>();
	private final Grid<Map<String, Object>> spenderGrid = new Grid<>();

	public DashboardView(DashboardService dashboardService, ProductService productService,
			WarehouseService warehouseService, SupplierService supplierService,
			CustomerService customerService, PurchaseOrderService purchaseOrderService,
			SalesOrderService salesOrderService) {
		this.dashboardService = dashboardService;
		this.productService = productService;
		this.warehouseService = warehouseService;
		this.supplierService = supplierService;
		this.customerService = customerService;
		this.purchaseOrderService = purchaseOrderService;
		this.salesOrderService = salesOrderService;

		setSizeFull();
		setPadding(true);
		setSpacing(true);

		addHeader();
		addMetricCards();
		addDetailSections();
	}

	private void addHeader() {
		H2 title = new H2("Dashboard");
		title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.Bottom.NONE);
		Paragraph desc = new Paragraph("Cross-module operational overview.");
		desc.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.Margin.Bottom.MEDIUM);
		add(title, desc);
	}

	private void addMetricCards() {
		// --- Master Data ---
		long productCount = productService.count();
		long warehouseCount = warehouseService.count();
		long supplierCount = supplierService.count();
		long customerCount = customerService.count();

		HorizontalLayout masterRow = new HorizontalLayout();
		masterRow.setWidthFull();
		masterRow.setSpacing(true);
		masterRow.add(
				createStatCard("Total Products", String.valueOf(productCount), VaadinIcon.PACKAGE,
						"app-icon-chip--info"),
				createStatCard("Total Warehouses", String.valueOf(warehouseCount), VaadinIcon.BUILDING,
						"app-icon-chip--warning"),
				createStatCard("Total Suppliers", String.valueOf(supplierCount), VaadinIcon.TRUCK,
						"app-icon-chip--success"),
				createStatCard("Total Customers", String.valueOf(customerCount), VaadinIcon.USERS,
						"app-icon-chip--danger"));
		add(masterRow);

		// --- Transactions ---
		long requisitionCount = dashboardService.countRequisitions();
		long pendingCount = dashboardService.countRequisitionsByStatus(ApprovalStatus.SUBMIT);
		long poCount = purchaseOrderService.count();
		long soCount = salesOrderService.count();

		HorizontalLayout txnRow = new HorizontalLayout();
		txnRow.setWidthFull();
		txnRow.setSpacing(true);
		txnRow.add(
				createStatCard("Total Requisitions", String.valueOf(requisitionCount), VaadinIcon.FILE_TEXT,
						"app-icon-chip--info"),
				createStatCard("Pending Approvals", String.valueOf(pendingCount), VaadinIcon.CLOCK,
						"app-icon-chip--warning"),
				createStatCard("Purchase Orders", String.valueOf(poCount), VaadinIcon.CART,
						"app-icon-chip--success"),
				createStatCard("Sales Orders", String.valueOf(soCount), VaadinIcon.ABACUS,
						"app-icon-chip--danger"));
		add(txnRow);
	}

	private Component createStatCard(String title, String value, VaadinIcon iconType, String chipClass) {
		VerticalLayout card = new VerticalLayout();
		card.setPadding(true);
		card.setSpacing(false);
		card.addClassName("app-metric-card");

		HorizontalLayout topRow = new HorizontalLayout();
		topRow.setWidthFull();
		topRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		topRow.setAlignItems(FlexComponent.Alignment.CENTER);

		Span titleSpan = new Span(title);
		titleSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.FontWeight.MEDIUM,
				LumoUtility.TextColor.SECONDARY);

		Icon icon = iconType.create();

		HorizontalLayout iconWrapper = new HorizontalLayout(icon);
		iconWrapper.addClassNames("app-icon-chip", chipClass);

		topRow.add(titleSpan, iconWrapper);

		Span valSpan = new Span(value);
		valSpan.addClassNames(LumoUtility.FontSize.XXLARGE, LumoUtility.FontWeight.BOLD,
				LumoUtility.TextColor.HEADER);
		valSpan.getStyle().set("margin-top", "12px");

		card.add(topRow, valSpan);
		return card;
	}

	private void addDetailSections() {
		add(createRecentSection());
		add(createSpendersSection());
	}

	private Component createRecentSection() {
		VerticalLayout section = new VerticalLayout();
		section.setPadding(true);
		section.addClassName("app-card");

		H3 title = new H3("Recent Requisitions");
		title.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.SEMIBOLD);

		recentGrid.addColumn(r -> r.referenceId).setHeader("Reference").setFlexGrow(1);
		recentGrid.addColumn(r -> r.title).setHeader("Title").setFlexGrow(2);
		recentGrid.addColumn(r -> r.department != null ? r.department.name : "-").setHeader("Department")
				.setFlexGrow(1);
		recentGrid.addColumn(r -> r.status != null ? r.status.name() : "-").setHeader("Status")
				.setFlexGrow(1);
		recentGrid.addColumn(r -> r.dueDate != null ? r.dueDate.format(DATE_FMT) : "-").setHeader("Due Date")
				.setFlexGrow(1);
		recentGrid.addColumn(r -> formatAmount(calculateTotal(r))).setHeader("Total Amount").setFlexGrow(1);
		recentGrid.setHeight("250px");

		loadRecentData();

		section.add(title, recentGrid);
		return section;
	}

	private Component createSpendersSection() {
		VerticalLayout section = new VerticalLayout();
		section.setPadding(true);
		section.addClassName("app-card");

		H3 title = new H3("Top Department Spenders");
		title.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.SEMIBOLD);

		spenderGrid.addColumn(map -> map.getOrDefault("dept_name", "-")).setHeader("Department")
				.setFlexGrow(2);
		spenderGrid.addColumn(
				map -> formatAmount((BigDecimal) map.getOrDefault("total_amount", BigDecimal.ZERO)))
				.setHeader("Total Spend").setFlexGrow(1);
		spenderGrid.addColumn(map -> map.getOrDefault("count_pr", "0")).setHeader("Requisitions")
				.setFlexGrow(1);
		spenderGrid.setHeight("250px");

		loadSpenderData();

		section.add(title, spenderGrid);
		return section;
	}

	private void loadRecentData() {
		try {
			List<Requisition> recent = dashboardService.getRecentRequisitions(5);
			recentGrid.setItems(recent);
		} catch (Exception ex) {
			// handle gracefully — grid stays empty
			ex.printStackTrace();
		}
	}

	private void loadSpenderData() {
		try {
			var response = dashboardService.getAllSpender();
			if (response != null) {
				spenderGrid.setItems(response);
			}
		} catch (Exception ex) {
			// handle gracefully — grid stays empty
			ex.printStackTrace();
		}
	}

	private String formatAmount(BigDecimal amount) {
		if (amount == null) {
			return "0";
		}
		return String.format("%,.0f", amount);
	}

	private BigDecimal calculateTotal(Requisition requisition) {
		BigDecimal total = BigDecimal.ZERO;
		if (requisition.items != null) {
			for (var item : requisition.items) {
				if (item.unitPrice != null && item.quantity != null) {
					total = total.add(item.unitPrice.multiply(BigDecimal.valueOf(item.quantity)));
				}
			}
		}
		return total;
	}
}
