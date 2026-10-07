package id.putra.core_erp.modules.procurement.views;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.enums.RequisitionStatus;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.service.DepartmentService;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.service.RequisitionService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.security.Authenticated;

@Route(value = "requisition/create", layout = MainLayout.class)
@PageTitle("Requisition | PR System")
@Authenticated
public class RequisitionFormView extends VerticalLayout implements HasUrlParameter<String> {

	private final RequisitionService requisitionService;
	private final DepartmentService departmentService;

	private Long requisitionId = null;
	private String currentStatus = "DRAFT";

	private final Binder<Requisition> binder = new BeanValidationBinder<>(Requisition.class);

	private final H2 titleHeader = new H2("Create Purchase Requisition");
	private final TextField titleField = new TextField("PR Title");
	private final ComboBox<Department> deptCombo = new ComboBox<>("Department");
	private final DatePicker dueDatePicker = new DatePicker("Due Date");
	private final TextField budgetDetailIdField = new TextField("Budget Detail Reference ID");
	private final TextArea noteField = new TextArea("Requisition Notes / Justification");

	private final Span grandTotalSpan = new Span("Total: IDR 0");

	private final Button saveDraftBtn = new Button("Save Draft");
	private final Button submitBtn = new Button("Submit for Approval");
	private final Button approveBtn = new Button("Approve");
	private final Button rejectBtn = new Button("Reject");
	private final Button backBtn = new Button("Back to Requisitions", VaadinIcon.ARROW_LEFT.create(),
			e -> UI.getCurrent().navigate(RequisitionListView.class));

	public RequisitionFormView(RequisitionService requisitionService, DepartmentService departmentService) {
		this.requisitionService = requisitionService;
		this.departmentService = departmentService;

		configureValidation();

		setSizeFull();
		setPadding(true);
		setSpacing(true);

		HorizontalLayout topBar = new HorizontalLayout(backBtn);
		topBar.setAlignItems(FlexComponent.Alignment.CENTER);

		add(topBar, titleHeader, createFormSection(), createItemsSection(), createActionButtons());
	}

	private void configureValidation() {
		binder.forField(titleField)
				.asRequired("Title is required")
				.bind("title");

		binder.forField(deptCombo)
				.asRequired("Department is required")
				.bind("department");

		binder.forField(dueDatePicker)
				.asRequired("Due date is required")
				.withConverter(
						localDate -> localDate != null
								? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
								: null,
						date -> date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null,
						"Invalid Date")
				.bind("dueDate");

		binder.forField(noteField)
				.bind("note");
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		if (parameter != null && !parameter.isBlank()) {
			this.requisitionId = Long.parseLong(parameter);
			this.titleHeader.setText("Purchase Requisition: " + parameter);
			loadRequisition(Long.parseLong(parameter));
		} else {
			this.requisitionId = null;
			this.titleHeader.setText("Create Purchase Requisition");
			Requisition newReq = new Requisition();
			newReq.dueDate = Date.from(LocalDate.now().plusDays(14).atStartOfDay(ZoneId.systemDefault()).toInstant());
			binder.readBean(newReq);
		}
	}

	private VerticalLayout createFormSection() {
		VerticalLayout card = new VerticalLayout();
		card.setPadding(true);
		card.getStyle().set("background", "#ffffff").set("border-radius", "12px").set("border", "1px solid #e2e8f0");

		FormLayout form = new FormLayout();
		titleField.setRequired(true);
		titleField.setWidthFull();

		deptCombo.setItemLabelGenerator(d -> (d.code != null ? d.code : "") + " - " + (d.name != null ? d.name : ""));
		deptCombo.setItems(departmentService.findAll(null, null));
		deptCombo.setRequired(true);

		budgetDetailIdField.setPlaceholder("e.g. 1");
		budgetDetailIdField.setValue("1");

		form.add(titleField, deptCombo, dueDatePicker, budgetDetailIdField, noteField);
		form.setColspan(titleField, 2);
		form.setColspan(noteField, 2);

		card.add(form);
		return card;
	}

	private VerticalLayout createItemsSection() {
		VerticalLayout card = new VerticalLayout();
		card.setPadding(true);
		card.getStyle().set("background", "#ffffff").set("border-radius", "12px").set("border", "1px solid #e2e8f0");

		H3 itemsTitle = new H3("Line Items");
		itemsTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.SEMIBOLD);

		Button addItemBtn = new Button("Add Item", VaadinIcon.PLUS.create(), e -> openAddItemDialog());
		addItemBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);

		HorizontalLayout itemsBar = new HorizontalLayout(itemsTitle, addItemBtn);
		itemsBar.setWidthFull();
		itemsBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		itemsBar.setAlignItems(FlexComponent.Alignment.CENTER);

		grandTotalSpan.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD,
				LumoUtility.Margin.Left.AUTO);

		HorizontalLayout footer = new HorizontalLayout(grandTotalSpan);
		footer.setWidthFull();
		footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

		card.add(itemsBar, footer);
		return card;
	}

	private HorizontalLayout createActionButtons() {
		HorizontalLayout actions = new HorizontalLayout();
		actions.setSpacing(true);
		actions.addClassNames(LumoUtility.Margin.Top.MEDIUM);

		saveDraftBtn.addClickListener(e -> saveRequisition(RequisitionStatus.DRAFT.getValue()));

		submitBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		submitBtn.addClickListener(e -> saveRequisition(RequisitionStatus.SEND.getValue()));

		approveBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
		approveBtn.addClickListener(e -> handleApproval(RequisitionStatus.APPROVED.getValue()));
		approveBtn.setVisible(false);

		rejectBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
		rejectBtn.addClickListener(e -> openRejectDialog());
		rejectBtn.setVisible(false);

		actions.add(saveDraftBtn, submitBtn, approveBtn, rejectBtn);
		return actions;
	}

	private void openAddItemDialog() {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Add Line Item");
		dialog.setWidth("500px");

		FormLayout form = new FormLayout();
		TextField nameField = new TextField("Item Name");
		nameField.setRequired(true);
		TextField itemNumberField = new TextField("Item Number / Code");
		TextField uomField = new TextField("UOM");
		uomField.setValue("PCS");
		TextField qtyField = new TextField("Quantity");
		qtyField.setValue("1");
		TextField unitPriceField = new TextField("Unit Price");
		unitPriceField.setValue("0");
		TextArea descField = new TextArea("Description");

		form.add(nameField, itemNumberField, uomField, qtyField, unitPriceField, descField);
		form.setColspan(descField, 2);

		Button addBtn = new Button("Add to List", e -> {
			if (nameField.getValue().isBlank()) {
				Notification.show("Item name is required", 3000, Notification.Position.TOP_CENTER);
				return;
			}

			dialog.close();
		});
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button cancelBtn = new Button("Cancel", ev -> dialog.close());

		dialog.add(form);
		dialog.getFooter().add(cancelBtn, addBtn);
		dialog.open();
	}

	private void loadRequisition(Long id) {
		try {
			Requisition r = requisitionService.findById(id);
			if (r != null && r.id != null) {
				this.currentStatus = r.status != null ? r.status : "DRAFT";
				binder.readBean(r);
				if (r.budget != null) {
					budgetDetailIdField.setValue(String.valueOf(r.budget.id));
				}
			}
		} catch (Exception ex) {
			Notification.show("Error loading PR: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	private void saveRequisition(String status) {
		Requisition params = new Requisition();
		if (this.requisitionId != null) {
			params.id = this.requisitionId;
		}
		params.status = status;
		params.budgetDetailId = budgetDetailIdField.getValue() != null && !budgetDetailIdField.getValue().isBlank()
				? Long.parseLong(budgetDetailIdField.getValue())
				: 1L;

		try {
			if (binder.writeBeanIfValid(params)) {
				if (params.id != null) {
					requisitionService.update(params);
				} else {
					requisitionService.save(params);
				}
				Notification.show("Requisition saved successfully", 3000, Notification.Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				UI.getCurrent().navigate(RequisitionListView.class);
			} else {
				Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		} catch (Exception ex) {
			Notification.show("Error: " + ex.getMessage(), 4000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	private void handleApproval(String status) {
		saveRequisition(status);
	}

	private void openRejectDialog() {
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
			noteField.setValue(noteField.getValue() + " [REJECT REASON: " + rejectReason.getValue() + "]");
			dialog.close();
			saveRequisition(RequisitionStatus.REJECT.getValue());
		});
		rejectConfirm.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

		Button cancel = new Button("Cancel", e -> dialog.close());

		dialog.add(rejectReason);
		dialog.getFooter().add(cancel, rejectConfirm);
		dialog.open();
	}
}
