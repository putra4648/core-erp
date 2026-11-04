package id.putra.core_erp.modules.procurement.views;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.service.BudgetService;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.service.DepartmentService;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.model.RequisitionItem;
import id.putra.core_erp.modules.procurement.service.RequisitionService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;

@Route(value = "requisition/create", layout = MainLayout.class)
@PageTitle("Requisition | PR System")
@Authenticated
public class CreateRequisitionView extends VerticalLayout {
	@Inject
	SecurityIdentity identity;

	private final RequisitionService requisitionService;
	private final DepartmentService departmentService;
	private final BudgetService budgetService;

	private final Grid<RequisitionItem> itemGrid = new Grid<>();

	private final Binder<@NonNull Requisition> binder = new BeanValidationBinder<>(Requisition.class);
	private final Binder<@NonNull RequisitionItem> binderItem = new BeanValidationBinder<>(RequisitionItem.class);

	private final H2 titleHeader = new H2("Create Purchase Requisition");
	private final TextField titleField = new TextField("PR Title");
	private final ComboBox<Department> deptCombo = new ComboBox<>("Department");
	private final DatePicker dueDatePicker = new DatePicker("Due Date");
	private final ComboBox<Budget> budgetField = new ComboBox<>("Budget From");
	private final TextArea noteField = new TextArea("Requisition Notes / Justification");

	private final TextField itemNameField = new TextField();
	private final IntegerField itemQtyField = new IntegerField();
	private final BigDecimalField itemPriceField = new BigDecimalField();

	private final Editor<RequisitionItem> itemEditor;
	private final List<RequisitionItem> itemList = new ArrayList<>();

	public CreateRequisitionView(RequisitionService requisitionService, DepartmentService departmentService,
			BudgetService budgetService) {
		super();
		this.requisitionService = requisitionService;
		this.departmentService = departmentService;
		this.budgetService = budgetService;

		this.itemEditor = itemGrid.getEditor();
		this.itemEditor.setBuffered(true);

		assert binderItem != null;
		this.itemEditor.setBinder(binderItem);

		configureValidation();
		configureItemValidation();

		Button backBtn = new Button("Back", VaadinIcon.ARROW_LEFT.create());
		backBtn.addClickListener(e -> e.getUI().navigate(RequisitionView.class));
		backBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		HorizontalLayout topBar = new HorizontalLayout(backBtn);
		topBar.setAlignItems(FlexComponent.Alignment.CENTER);

		add(topBar, titleHeader, createFormSection(), createItemsSection(), createActionButtons());
	}

	private void configureValidation() {
		binder.bind(titleField, "title");
		binder.bind(budgetField, "budget");
		binder.bind(deptCombo, "department");
		binder.bind(dueDatePicker, "dueDate");
		binder.bind(noteField, "note");
	}

	private void configureItemValidation() {
		binderItem.bind(itemNameField, "name");
		binderItem.bind(itemQtyField, "quantity");
		binderItem.bind(itemPriceField, "unitPrice");
	}

	private Component createFormSection() {
		VerticalLayout card = new VerticalLayout();
		card.setPadding(true);
		card.addClassName("app-card");

		FormLayout form = new FormLayout();
		titleField.setRequired(true);
		titleField.setWidthFull();

		deptCombo.setItemLabelGenerator(d -> (d.code != null ? d.code : "") + " - " + (d.name != null ? d.name : ""));
		deptCombo.setItems(departmentService.findAll(null, null));
		deptCombo.setRequired(true);

		budgetField.setPlaceholder("e.g. 1");
		budgetField.setItemLabelGenerator(b -> (b.name != null ? b.name : ""));
		budgetField.setItems(budgetService.findAll(null, null));
		budgetField.setRequired(true);

		form.add(titleField, deptCombo, dueDatePicker, budgetField, noteField);
		form.setColspan(titleField, 2);
		form.setColspan(noteField, 2);

		card.add(form);
		return card;
	}

	private Component createItemsSection() {
		VerticalLayout card = new VerticalLayout();
		card.setPadding(true);
		card.addClassName("app-card");

		H3 itemsTitle = new H3("Items");
		itemsTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.SEMIBOLD);

		Button addItemBtn = new Button("Add Item", VaadinIcon.PLUS.create(), e -> {
			itemList.add(new RequisitionItem());
			itemGrid.getDataProvider().refreshAll();
		});
		addItemBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);

		HorizontalLayout itemsBar = new HorizontalLayout(itemsTitle, addItemBtn);
		itemsBar.setWidthFull();
		itemsBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		itemsBar.setAlignItems(FlexComponent.Alignment.CENTER);

		configureGrid();

		card.add(itemsBar, itemGrid);
		return card;
	}

	private Component createActionButtons() {
		HorizontalLayout actions = new HorizontalLayout();
		actions.setSpacing(true);
		actions.addClassNames(LumoUtility.Margin.Top.MEDIUM);

		Button submitBtn = new Button("Submit", VaadinIcon.CHECK.create());
		submitBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		submitBtn.addClickListener(e -> saveRequisition());

		actions.add(submitBtn);
		return actions;
	}

	private void configureGrid() {
		itemGrid.setHeight("50vh");
		itemGrid.addColumn(s -> s.name)
				.setHeader("Name")
				.setKey("name")
				.setEditorComponent(itemNameField)
				.setAutoWidth(true);

		itemGrid.addColumn(s -> s.quantity)
				.setHeader("Quantity")
				.setKey("quantity")
				.setEditorComponent(itemQtyField)
				.setAutoWidth(true);

		itemGrid.addColumn(s -> s.unitPrice)
				.setHeader("Unit Price")
				.setKey("unitPrice")
				.setEditorComponent(itemPriceField)
				.setAutoWidth(true);

		itemGrid.addComponentColumn(param -> {
			Button editButton = new Button(VaadinIcon.EDIT.create());
			editButton.addClickListener(e -> {
				if (itemEditor.isOpen())
					itemEditor.cancel();
				itemGrid.getEditor().editItem(param);
			});
			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> {
				itemList.remove(param);
				itemGrid.getDataProvider().refreshAll();
			});
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
			var layout = new HorizontalLayout(editButton, deleteBtn);

			return layout;
		}).setEditorComponent(param -> {
			Button saveBtn = new Button(VaadinIcon.DISC.create(), e -> {
				// stepEditor.save() mengembalikan true jika semua validasi Binder lolos
				if (itemEditor.save()) {
					itemGrid.getDataProvider().refreshAll();
					Notification.show("Item berhasil disimpan", 2000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				} else {
					Notification
							.show("Mohon lengkapi field yang bermasalah pada Item", 3000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
			});
			Button cancelBtn = new Button(VaadinIcon.CLOSE.create(), e -> itemEditor.cancel());

			var layout = new HorizontalLayout(saveBtn, cancelBtn);

			return layout;
		}).setFrozenToEnd(true).setHeader("Actions").setAutoWidth(true);

		itemGrid.setItems(itemList);

	}

	private void saveRequisition() {
		try {
			if (itemEditor.isOpen()) {
				Notification
						.show("Selesaikan atau batalkan edit baris Step terlebih dahulu", 3000,
								Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
				return;
			}

			// Validasi 2: Minimal harus ada 1 step
			if (itemList.isEmpty()) {
				Notification
						.show("Requisition harus memiliki minimal 1 Item", 3000,
								Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
				return;
			}

			var params = new Requisition();
			params.budget = budgetField.getValue();
			params.status = ApprovalStatus.SUBMIT;
			params.dueDate = dueDatePicker.getValue();
			params.department = deptCombo.getValue();
			params.note = noteField.getValue();
			params.title = titleField.getValue();
			params.referenceId = "PR-" + System.currentTimeMillis();

			if (binder.writeBeanIfValid(params)) {
				// Populate items AFTER binder validation so the binder doesn't clear the list
				itemList.forEach(item -> {
					item.requisition = params;
					params.items.add(item);
				});

				requisitionService.submit(params, identity);
				Notification.show("Requisition saved successfully", 3000, Notification.Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				UI.getCurrent().navigate(RequisitionView.class);
			} else {
				Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		} catch (Exception ex) {
			Notification.show("Error: " + ex.getMessage(), 4000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

}
