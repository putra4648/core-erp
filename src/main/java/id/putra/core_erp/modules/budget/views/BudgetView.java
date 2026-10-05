package id.putra.core_erp.modules.budget.views;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.enums.BudgetType;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.service.BudgetService;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "budgets", layout = MainLayout.class)
@PageTitle("Budgets | Purchase Requisition System")
@Authenticated
public class BudgetView extends VerticalLayout {

	private final BudgetService budgetService;
	private final Grid<Budget> grid = new Grid<>();
	private final List<Department> departmentList = new ArrayList<>();

	public BudgetView(BudgetService budgetService, DepartmentRepo departmentRepo) {
		this.budgetService = budgetService;

		setSizeFull();
		setPadding(true);
		setSpacing(true);

		departmentRepo.findAll().list().forEach(departmentList::add);

		H2 header = new H2("Budget Planning & Management");
		header.addClassNames(LumoUtility.FontSize.XLARGE);

		DatePicker.DatePickerI18n datePickerI18n = new DatePicker.DatePickerI18n();
		datePickerI18n.setDateFormats("yyyy");

		Button addBtn = new Button("Add Budget", VaadinIcon.PLUS.create(), e -> openAddBudgetDialog());
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshBudgets());

		HorizontalLayout toolbar = new HorizontalLayout(addBtn, refreshBtn);
		toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);

		configureGrid();

		add(header, toolbar, grid);

	}

	private void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(b -> b.id != null ? b.id : "-").setHeader("Budget No").setAutoWidth(true);
		grid.addColumn(b -> b.getDepartment().getName() != null ? b.getDepartment().getName() : "-")
				.setHeader("Department");
		grid.addColumn(b -> b.getFiscalYear() != null ? b.getFiscalYear() : "").setHeader("Year").setAutoWidth(true);
		grid.addColumn(b -> b.getAllocated() != null ? b.getAllocated() : "").setHeader("Amount Allocated (Year)")
				.setAutoWidth(true);

		refreshBudgets();
	}

	private void refreshBudgets() {
		List<Budget> res = new ArrayList<>();
		var budget = new Budget();
		budgetService.findAll(budget, Page.ofSize(100)).forEach(res::add);
		grid.setItems(res);
	}

	private void openAddBudgetDialog() {

		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Add Budget");
		dialog.setWidth("500px");

		FormLayout form = new FormLayout();

		ComboBox<Department> deptCombo = new ComboBox<>("Select Department");
		deptCombo.setWidth("280px");
		deptCombo.setItemLabelGenerator(d -> d.getCode() + " - " + d.getName());
		deptCombo.setItems(departmentList);
		deptCombo.addValueChangeListener(e -> refreshBudgets());
		deptCombo.setRequired(true);

		LocalDate now = LocalDate.now(ZoneId.systemDefault());
		ComboBox<Integer> yearCombo = new ComboBox<>("Fiscal Year");
		List<Integer> selectableYears = IntStream.range(now.getYear() - 20, now.getYear() + 1).boxed().toList();
		yearCombo.setWidth("140px");
		yearCombo.setItems(selectableYears);
		yearCombo.setValue(now.getYear());
		yearCombo.addValueChangeListener(e -> refreshBudgets());
		yearCombo.setRequired(true);

		TextField nameField = new TextField("Budget Name");
		nameField.setRequired(true);

		ComboBox<BudgetType> typeCombo = new ComboBox<>("Budget Type");
		typeCombo.setItems(BudgetType.values());
		typeCombo.setValue(BudgetType.EXPENSE);

		NumberField amountField = new NumberField("Annual Allocation (Total)");
		TextArea noteField = new TextArea("Notes");

		form.add(deptCombo, yearCombo, nameField, typeCombo, amountField, noteField);

		Button saveBtn = new Button("Save", e -> {
			try {
				Budget budget = new Budget();
				budget.setDepartment(deptCombo.getValue());
				budget.setFiscalYear(yearCombo.getValue().toString());
				budget.setAllocated(amountField.getValue().longValue());

				budgetService.save(budget);
				Notification.show("Budget saved successfully", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				dialog.close();
				refreshBudgets();

			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button cancelBtn = new Button("Cancel", ev -> dialog.close());

		dialog.add(form);
		dialog.getFooter().add(cancelBtn, saveBtn);
		dialog.open();
	}
}
