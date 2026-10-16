package id.putra.core_erp.modules.procurement.views;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import id.putra.core_erp.modules.procurement.service.DashboardService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.security.Authenticated;

@Route(value = "requisition/dashboard", layout = MainLayout.class)
@PageTitle("PR Dashboard | CORE ERP")
@Authenticated
public class RequisitionDashboardView extends ViewWithDialog {

	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private final DashboardService dashboardService;
	private final RequisitionRepo requisitionRepo;

	private final Grid<Map<String, Object>> spenderGrid = new Grid<>();
	private final Grid<Requisition> recentGrid = new Grid<>();

	public RequisitionDashboardView(DashboardService dashboardService, RequisitionRepo requisitionRepo) {
		super();
		this.dashboardService = dashboardService;
		this.requisitionRepo = requisitionRepo;

		addHeader();
		addToolbar();
	}

	@Override
	public void addHeader() {
		H2 header = new H2("Requisition Dashboard");
		header.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
		add(header);
	}

	@Override
	public void addToolbar() {
		// --- Requisition status breakdown ---
		long totalCount = requisitionRepo.count();
		long pendingCount = requisitionRepo.count("status = ?1", ApprovalStatus.SUBMIT);
		long approvedCount = requisitionRepo.count("status = ?1", ApprovalStatus.APPROVED);
		long rejectedCount = requisitionRepo.count("status = ?1", ApprovalStatus.REJECTED);

		HorizontalLayout stats = new HorizontalLayout();
		stats.setWidthFull();
		stats.setSpacing(true);
		stats.add(
				createStatCard("Total Requisitions", String.valueOf(totalCount), VaadinIcon.FILE_TEXT,
						"#2563eb", "#dbeafe"),
				createStatCard("Pending Approval", String.valueOf(pendingCount), VaadinIcon.CLOCK,
						"#d97706", "#fef3c7"),
				createStatCard("Approved", String.valueOf(approvedCount), VaadinIcon.CHECK_CIRCLE,
						"#059669", "#d1fae5"),
				createStatCard("Rejected", String.valueOf(rejectedCount), VaadinIcon.CLOSE_CIRCLE,
						"#dc2626", "#fee2e2"));
		add(stats);

		// --- Detail sections ---
		add(createSpendersSection());
		add(createRecentSection());
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
		section.getStyle().set("background", "#ffffff").set("border-radius", "12px")
				.set("box-shadow", "0 1px 3px 0 rgba(0, 0, 0, 0.1)").set("border", "1px solid #e2e8f0");

		H3 title = new H3("Top Department Spenders");
		title.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.SEMIBOLD);

		spenderGrid.addColumn(map -> map.getOrDefault("dept_name", "-")).setHeader("Department")
				.setFlexGrow(2);
		spenderGrid.addColumn(
				map -> formatAmount((BigDecimal) map.getOrDefault("total_amount", BigDecimal.ZERO)))
				.setHeader("Total Spend").setFlexGrow(1);
		spenderGrid.addColumn(map -> map.getOrDefault("count_pr", "0")).setHeader("Requisitions")
				.setFlexGrow(1);
		spenderGrid.setHeight("200px");

		loadSpenderData();

		section.add(title, spenderGrid);
		return section;
	}

	private Component createRecentSection() {
		VerticalLayout section = new VerticalLayout();
		section.setPadding(true);
		section.getStyle().set("background", "#ffffff").set("border-radius", "12px")
				.set("box-shadow", "0 1px 3px 0 rgba(0, 0, 0, 0.1)").set("border", "1px solid #e2e8f0");

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
		recentGrid.setHeight("300px");

		loadRecentData();

		section.add(title, recentGrid);
		return section;
	}

	private void loadSpenderData() {
		try {
			var response = dashboardService.getAllSpender();
			if (response != null) {
				spenderGrid.setItems(response);
			}
		} catch (Exception ex) {
			// handle gracefully — grid stays empty
		}
	}

	private void loadRecentData() {
		try {
			List<Requisition> recent = dashboardService.getRecentRequisitions(10);
			recentGrid.setItems(recent);
		} catch (Exception ex) {
			// handle gracefully — grid stays empty
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

	@Override
	public void refreshData() {
		loadSpenderData();
		loadRecentData();
	}

	@Override
	public void configureGrid() {
		// No-op — grids are configured inline in their section builders
	}

	@Override
	protected void buildAndShowDialog(Object entity) throws Exception {
		// No-op — dashboard is read-only
	}

	@Override
	public void confirmDelete(Object entity) {
		// No-op — dashboard is read-only
	}
}
