package id.putra.core_erp.modules.procurement.views;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.modules.approval.service.ApprovalWorkflowService;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.service.RequisitionService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;

@Route(value = "requisition", layout = MainLayout.class)
@PageTitle("Purchase Requisitions | PR System")
@Authenticated
public class RequisitionView extends VerticalLayout {
	@Inject
	SecurityIdentity identity;

	private final RequisitionService requisitionService;
	private final ApprovalWorkflowService approvalWorkflowService;

	private final TextField searchField = new TextField();
	private final ComboBox<String> statusFilter = new ComboBox<>();
	private final Grid<Requisition> grid = new Grid<>();
	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public RequisitionView(RequisitionService requisitionService, ApprovalWorkflowService approvalWorkflowService) {
		this.requisitionService = requisitionService;
		this.approvalWorkflowService = approvalWorkflowService;

		setSizeFull();
		setPadding(true);
		setSpacing(true);

		addHeader();
		addToolbar();
		configureGrid();

	}

	public void addHeader() {
		H2 header = new H2("Purchase Requisitions");
		header.addClassNames(LumoUtility.FontSize.XLARGE);
		add(header);
	}

	public void addToolbar() {
		Button newPrBtn = new Button("Create Requisition", VaadinIcon.PLUS.create(),
				e -> UI.getCurrent().navigate(CreateRequisitionView.class));
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

	public void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(r -> r.id != null ? r.id : "-").setHeader("PR Number").setAutoWidth(true);
		grid.addColumn(r -> r.title != null ? r.title : "-").setHeader("Title").setFlexGrow(2);
		grid.addColumn(r -> r.department != null ? r.department.name : "-").setHeader("Department")
				.setFlexGrow(1);
		grid.addColumn(r -> r.dueDate != null ? DATE_FMT.format(r.dueDate) : "-").setHeader("Due Date")
				.setAutoWidth(true);

		grid.addComponentColumn(r -> {
			String statusName = r.status != null ? r.status.name() : "DRAFT";
			Span badge = new Span(statusName);
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		grid.addComponentColumn(r -> {
			boolean isSubmit = r.status == ApprovalStatus.SUBMIT;

			Button viewBtn = new Button("View", VaadinIcon.EYE.create());
			viewBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
			viewBtn.addClickListener(e -> UI.getCurrent().navigate(ViewRequisitionView.class, r.id));

			Button approveBtn = new Button("Approve", VaadinIcon.THUMBS_UP.create());
			approveBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY,
					ButtonVariant.LUMO_SMALL);
			approveBtn.addClickListener(e -> requisitionApprove(r.id));
			approveBtn.setVisible(isSubmit);

			Button rejectBtn = new Button("Reject", VaadinIcon.THUMBS_DOWN.create());
			rejectBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
			rejectBtn.addClickListener(e -> openRejectDialog(r.id));
			rejectBtn.setVisible(isSubmit);

			var layout = new HorizontalLayout(viewBtn);

			var approvalWorkflow = approvalWorkflowService.findByRequestType(ApprovalType.REQUISITION);

			if (approvalWorkflow != null) {
				var roles = approvalWorkflow.steps.stream().map(a -> a.roleRequired).toList();
				if (roles.stream().anyMatch(a -> identity.hasRole(a))) {
					layout.add(approveBtn);
					layout.add(rejectBtn);
				}
			}

			return layout;
		}).setHeader("Actions").setAutoWidth(true);

		add(grid);

		refreshData();
	}

	public void refreshData() {
		try {
			Requisition params = new Requisition();
			List<Requisition> res = requisitionService.findAll(params, Page.ofSize(100));
			grid.setItems(res);
		} catch (Exception ex) {
			Notification.show("Error loading requisitions: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	private void requisitionApprove(Long id) {
		try {
			requisitionService.approve(id, identity);
			Notification.show("Requisition approved", 3000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		} catch (Exception ex) {
			Notification.show("Error: " + ex.getMessage(), 4000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	private void openRejectDialog(Long id) {
		try {
			Dialog dialog = new Dialog();
			dialog.setHeaderTitle("Reject Purchase Requisition");
			dialog.setWidth("450px");

			TextArea rejectReason = new TextArea("Rejection Reason");
			rejectReason.setRequired(true);
			rejectReason.setWidthFull();

			Button rejectConfirm = new Button("Confirm Rejection", e -> {
				if (rejectReason.getValue().isBlank()) {
					Notification.show("Reason is required", 3000, Notification.Position.TOP_CENTER);
					return;
				}
				// noteField.setValue(noteField.getValue() + " [REJECT REASON: " +
				// rejectReason.getValue() + "]");
				dialog.close();
				try {
					requisitionService.reject(id, identity);
					Notification.show("Requisition rejected", 3000, Notification.Position.TOP_CENTER)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				} catch (Exception ex) {
					Notification.show("Error: " + ex.getMessage(), 4000, Notification.Position.TOP_CENTER)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			});
			rejectConfirm.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

			Button cancel = new Button("Cancel", e -> dialog.close());

			dialog.add(rejectReason);
			dialog.getFooter().add(cancel, rejectConfirm);
			dialog.open();
		} catch (Exception e) {
			Notification.show("Error: " + e.getMessage(), 4000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
}
