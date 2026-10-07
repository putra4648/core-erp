package id.putra.core_erp.modules.procurement.views;

import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.modules.procurement.repository.ItemRepo;
import id.putra.core_erp.modules.procurement.repository.QuotationRepo;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import id.putra.core_erp.modules.procurement.repository.SupplierRepo;
import id.putra.core_erp.modules.procurement.service.DashboardService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.security.Authenticated;

@Route(value = "requisition/dashboard", layout = MainLayout.class)
@PageTitle("PR Dashboard | CORE ERP")
@Authenticated
public class RequisitionDashboardView extends ViewWithDialog {

	private final DashboardService dashboardService;
	private final RequisitionRepo requisitionRepo;
	private final QuotationRepo rfqRepo;
	private final SupplierRepo supplierRepo;
	private final ItemRepo itemRepo;

	public RequisitionDashboardView(DashboardService dashboardService, RequisitionRepo requisitionRepo,
			QuotationRepo rfqRepo, SupplierRepo supplierRepo, ItemRepo itemRepo) {
		super();
		this.dashboardService = dashboardService;
		this.requisitionRepo = requisitionRepo;
		this.rfqRepo = rfqRepo;
		this.supplierRepo = supplierRepo;
		this.itemRepo = itemRepo;

		addHeader();
		addToolbar();
		// configureGrid();

	}

	@Override
	public void addHeader() {
		H2 header = new H2("Overview Dashboard");
		header.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
		add(header);
	}

	@Override
	public void addToolbar() {
		HorizontalLayout stats = new HorizontalLayout();
		stats.setWidthFull();
		stats.setSpacing(true);

		long prCount = requisitionRepo.count();
		long rfqCount = rfqRepo.count();
		long supplierCount = supplierRepo.count();
		long itemCount = itemRepo.count();

		stats.add(
				createStatCard("Total Requisitions", String.valueOf(prCount), VaadinIcon.FILE_TEXT, "#2563eb",
						"#dbeafe"),
				createStatCard("Active RFQs", String.valueOf(rfqCount), VaadinIcon.SCALE, "#059669", "#d1fae5"),
				createStatCard("Registered Suppliers", String.valueOf(supplierCount), VaadinIcon.TRUCK, "#d97706",
						"#fef3c7"),
				createStatCard("Catalog Items", String.valueOf(itemCount), VaadinIcon.PACKAGE, "#7c3aed", "#ede9fe"));

		add(stats);

		add(createSpendersSection());
	}

	private Component createStatCard(String title, String value, VaadinIcon iconType, String iconColor,
			String bgColor) {
		VerticalLayout card = new VerticalLayout();
		card.setPadding(true);
		card.setSpacing(false);
		card.getStyle().set("background", "#ffffff").set("border-radius", "12px")
				.set("box-shadow", "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px -1px rgba(0, 0, 0, 0.1)")
				.set("border", "1px solid #e2e8f0");

		HorizontalLayout topRow = new HorizontalLayout();
		topRow.setWidthFull();
		topRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		topRow.setAlignItems(FlexComponent.Alignment.CENTER);

		Span titleSpan = new Span(title);
		titleSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.FontWeight.MEDIUM,
				LumoUtility.TextColor.SECONDARY);

		Icon icon = iconType.create();
		icon.setSize("20px");
		icon.setColor(iconColor);

		HorizontalLayout iconWrapper = new HorizontalLayout(icon);
		iconWrapper.setAlignItems(FlexComponent.Alignment.CENTER);
		iconWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		iconWrapper.getStyle().set("background", bgColor).set("border-radius", "8px").set("width", "36px").set("height",
				"36px");

		topRow.add(titleSpan, iconWrapper);

		Span valSpan = new Span(value);
		valSpan.addClassNames(LumoUtility.FontSize.XXLARGE, LumoUtility.FontWeight.BOLD, LumoUtility.TextColor.HEADER);
		valSpan.getStyle().set("margin-top", "12px");

		card.add(topRow, valSpan);
		return card;
	}

	private Component createSpendersSection() {
		VerticalLayout section = new VerticalLayout();
		section.setPadding(true);
		section.setSizeFull();
		section.getStyle().set("background", "#ffffff").set("border-radius", "12px")
				.set("box-shadow", "0 1px 3px 0 rgba(0, 0, 0, 0.1)").set("border", "1px solid #e2e8f0");

		H3 title = new H3("Top Department Spenders");
		title.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.SEMIBOLD);

		Grid<Map<String, Object>> grid = new Grid<>();
		grid.addColumn(map -> map.getOrDefault("dept_name", "-")).setHeader("Department").setFlexGrow(2);
		grid.addColumn(map -> map.getOrDefault("total_amount", "0")).setHeader("Total Spend").setFlexGrow(1);
		grid.addColumn(map -> map.getOrDefault("count_pr", "0")).setHeader("Requisitions Count").setFlexGrow(1);

		try {
			var response = dashboardService.getAllSpender();
			if (response != null) {
				grid.setItems(response);
			}
		} catch (Exception ex) {
			// handle gracefully
		}

		section.add(title, grid);
		return section;
	}

	@Override
	public void refreshData() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'refreshData'");
	}

	@Override
	public void configureGrid() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'configureGrid'");
	}

	@Override
	public void openDialog(Object entity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'openDialog'");
	}

	@Override
	public void confirmDelete(Object entity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'confirmDelete'");
	}
}
