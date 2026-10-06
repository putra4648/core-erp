package id.putra.core_erp.modules.dashboard.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.product.service.ProductService;
import id.putra.core_erp.modules.warehouse.service.WarehouseService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.security.Authenticated;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard")
@Authenticated
public class DashboardView extends VerticalLayout {

	private final ProductService productService;
	private final WarehouseService warehouseService;

	public DashboardView(ProductService productService, WarehouseService warehouseService) {
		this.productService = productService;
		this.warehouseService = warehouseService;

		setSizeFull();
		setPadding(true);
		setSpacing(true);

		addHeader();
		addMetricCards();
		addRecentActivitySection();
	}

	private void addHeader() {
		H2 title = new H2("Dashboard");
		Paragraph desc = new Paragraph("Overview of warehouse operations and operational metrics.");
		add(title, desc);
	}

	private void addMetricCards() {
		long totalProducts = 0;
		long totalWarehouses = 0;

<<<<<<< HEAD
		// try {
		// totalProducts = productService.countGetProducts(new Product(),
		// Page.ofSize(0));
		// } catch (Exception ignored) {
		// }

		// try {
		// totalWarehouses = warehouseService.countGetWarehouses(new Warehouse(),
		// Page.ofSize(1));
		// } catch (Exception ignored) {
		// }

		HorizontalLayout cardsLayout = new HorizontalLayout();
		cardsLayout.setWidthFull();
		cardsLayout.setSpacing(true);

		cardsLayout.add(createKpiCard("Inbound Today", "12", VaadinIcon.DOWNLOAD.create(), "text-primary"),
				createKpiCard("Outbound Today", "8", VaadinIcon.UPLOAD.create(), "text-warning"),
				createKpiCard("Total Products", String.valueOf(totalProducts), VaadinIcon.PACKAGE.create(),
						"text-success"),
				createKpiCard("Total Warehouses", String.valueOf(totalWarehouses), VaadinIcon.BUILDING.create(),
						"text-info"));

		add(cardsLayout);
	}

	private void addRecentActivitySection() {
		H3 header = new H3("Recent Operational Activity");
		header.addClassNames("mt-l", "mb-s", "text-l", "font-semibold");

		Grid activityGrid = new Grid<>();
		activityGrid.setWidthFull();
		activityGrid.setAllRowsVisible(true);

		VerticalLayout section = new VerticalLayout(header, activityGrid);
		section.setPadding(false);
		section.setWidthFull();

		add(section);
	}

	private VerticalLayout createKpiCard(String title, String value, com.vaadin.flow.component.icon.Icon icon,
			String iconColorClass) {
		VerticalLayout card = new VerticalLayout();
		card.addClassNames("border", "rounded-l", "p-m", "bg-base");
		card.setWidthFull();

		HorizontalLayout header = new HorizontalLayout(icon, new Span(title));
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		header.addClassNames("text-secondary", "text-s");

		H3 val = new H3(value);
		val.addClassNames("m-0", "text-2xl", "font-bold");

		card.add(header, val);
		return card;
=======
		HorizontalLayout cardsLayout = new HorizontalLayout();
		cardsLayout.setWidthFull();
		cardsLayout.setSpacing(true);

		cardsLayout.add(createKpiCard("Inbound Today", "12", VaadinIcon.DOWNLOAD.create(), "text-primary"),
				createKpiCard("Outbound Today", "8", VaadinIcon.UPLOAD.create(), "text-warning"),
				createKpiCard("Total Products", String.valueOf(totalProducts), VaadinIcon.PACKAGE.create(),
						"text-success"),
				createKpiCard("Total Warehouses", String.valueOf(totalWarehouses), VaadinIcon.BUILDING.create(),
						"text-info"));

		add(cardsLayout);
	}

	private VerticalLayout createKpiCard(String title, String value, com.vaadin.flow.component.icon.Icon icon,
			String iconColorClass) {
		VerticalLayout card = new VerticalLayout();
		card.addClassNames("border", "rounded-l", "p-m", "bg-base");
		card.setWidthFull();

		HorizontalLayout header = new HorizontalLayout(icon, new Span(title));
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		header.addClassNames("text-secondary", "text-s");

		H3 val = new H3(value);
		val.addClassNames("m-0", "text-2xl", "font-bold");

		card.add(header, val);
		return card;
	}

	private void addRecentActivitySection() {
		H3 header = new H3("Recent Operational Activity");
		header.addClassNames("mt-l", "mb-s", "text-l", "font-semibold");

		Grid<ActivityItem> activityGrid = new Grid<>();
		activityGrid.setWidthFull();
		activityGrid.setAllRowsVisible(true);

		activityGrid.addComponentColumn(item -> {
			Avatar avatar = new Avatar(item.type());
			avatar.setAbbreviation(item.type().substring(0, 1).toUpperCase());
			Span title = new Span(item.title());
			title.addClassNames("font-medium");
			Span time = new Span(item.time());
			time.addClassNames("text-xs", "text-secondary");

			VerticalLayout info = new VerticalLayout(title, time);
			info.setPadding(false);
			info.setSpacing(false);

			HorizontalLayout layout = new HorizontalLayout(avatar, info);
			layout.setAlignItems(FlexComponent.Alignment.CENTER);
			return layout;
		}).setHeader("Activity").setFlexGrow(1);

		activityGrid.addComponentColumn(item -> {
			boolean isInbound = "inbound".equalsIgnoreCase(item.type());
			Span qty = new Span((isInbound ? "+" : "-") + item.quantity());
			qty.addClassNames("font-bold", isInbound ? "text-success" : "text-warning");
			return qty;
		}).setHeader("Quantity").setAutoWidth(true);

		List<ActivityItem> activities = List.of(new ActivityItem("inbound", "Received Nike Air Max", "10 mins ago", 50),
				new ActivityItem("outbound", "Shipped iPhone 15 Pro", "45 mins ago", 2),
				new ActivityItem("inbound", "Received Samsung S24", "2 hours ago", 20),
				new ActivityItem("outbound", "Shipped Sony PS5", "3 hours ago", 1));

		activityGrid.setItems(activities);

		VerticalLayout section = new VerticalLayout(header, activityGrid);
		section.setPadding(false);
		section.setWidthFull();

		add(section);
	}

	public static record ActivityItem(String type, String title, String time, int quantity) {
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}
}
