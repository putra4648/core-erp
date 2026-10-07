package id.putra.core_erp.modules.procurement.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import id.putra.core_erp.modules.procurement.service.RequisitionService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.security.Authenticated;

@Route(value = "requisition", layout = MainLayout.class)
@PageTitle("Purchase Requisitions | PR System")
@Authenticated
public class RequisitionListView extends ViewWithDialog {

	private final RequisitionService requisitionService;
	private final RequisitionRepo requisitionRepo;
	private final DepartmentRepo departmentRepo;

	private final TextField searchField = new TextField();
	private final ComboBox<String> statusFilter = new ComboBox<>();
	private final Grid<Requisition> grid = new Grid<>();
	private final List<Requisition> reqList = new ArrayList<>();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public RequisitionListView(RequisitionService requisitionService, RequisitionRepo requisitionRepo,
			DepartmentRepo departmentRepo) {
		this.requisitionService = requisitionService;
		this.requisitionRepo = requisitionRepo;
		this.departmentRepo = departmentRepo;

		addHeader();
		addToolbar();
		configureGrid();

	}

	@Override
	public void addHeader() {
		H2 header = new H2("Purchase Requisitions");
		header.addClassNames(LumoUtility.FontSize.XLARGE);
		add(header);
	}

	@Override
	public void addToolbar() {
		Button newPrBtn = new Button("Create Requisition", VaadinIcon.PLUS.create(),
				e -> UI.getCurrent().navigate(RequisitionFormView.class));
		newPrBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		searchField.setPlaceholder("Search PR by Title / ID...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.setWidth("280px");
		searchField.addValueChangeListener(e -> refreshData());

		statusFilter.setItems("ALL", "DRAFT", "WAITING_APPROVAL", "APPROVED", "REJECT", "PO_CREATED");
		statusFilter.setValue("ALL");
		statusFilter.setWidth("180px");
		statusFilter.addValueChangeListener(e -> refreshData());

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, statusFilter, newPrBtn, refreshBtn);
		toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

		add(toolbar);
	}

	@Override
	public void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(r -> r.id != null ? r.id : "-").setHeader("PR Number").setAutoWidth(true);
		grid.addColumn(r -> r.title != null ? r.title : "-").setHeader("Title").setFlexGrow(2);
		grid.addColumn(r -> r.department != null ? r.department.name : "-").setHeader("Department")
				.setFlexGrow(1);
		grid.addColumn(r -> r.dueDate != null ? dateFormat.format(r.dueDate) : "-").setHeader("Due Date")
				.setAutoWidth(true);

		grid.addComponentColumn(r -> {
			Span badge = new Span(r.status != null ? r.status : "DRAFT");
			String st = r.status != null ? r.status.toUpperCase() : "";
			if (st.contains("APPROV") || st.contains("PO_")) {
				badge.getElement().getThemeList().add("badge success");
			} else if (st.contains("REJECT")) {
				badge.getElement().getThemeList().add("badge error");
			} else if (st.contains("WAIT")) {
				badge.getElement().getThemeList().add("badge warning");
			} else {
				badge.getElement().getThemeList().add("badge");
			}
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		grid.addComponentColumn(r -> {
			Button viewBtn = new Button("View / Edit", VaadinIcon.EYE.create(),
					e -> UI.getCurrent().navigate(RequisitionFormView.class, r.id.toString()));
			viewBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);

			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> {
				if ("DRAFT".equalsIgnoreCase(r.status) || "REJECT".equalsIgnoreCase(r.status)) {
					requisitionRepo.deleteById(r.id);
					Notification.show("Requisition deleted", 2000, Notification.Position.TOP_CENTER);
					refreshData();
				} else {
					Notification.show("Cannot delete submitted requisition", 2000, Notification.Position.TOP_CENTER);
				}
			});
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);

			return new HorizontalLayout(viewBtn, deleteBtn);
		}).setHeader("Actions").setAutoWidth(true);

		add(grid);

		refreshData();
	}

	@Override
	public void refreshData() {
		try {
			Requisition params = new Requisition();

			String selectedStatus = statusFilter.getValue();
			if ("ALL".equalsIgnoreCase(selectedStatus) || selectedStatus == null) {
				params.status = "";
			} else {
				params.status = selectedStatus;
			}

			List<Requisition> res = requisitionService.findAll(params, null);
			reqList.clear();
			if (res != null) {
				reqList.addAll(res);
			}
			grid.setItems(reqList);
		} catch (Exception ex) {
			Notification.show("Error loading requisitions: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
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
