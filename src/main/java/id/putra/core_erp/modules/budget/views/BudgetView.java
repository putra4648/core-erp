package id.putra.core_erp.modules.budget.views;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.IntStream;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import id.putra.core_erp.enums.BudgetType;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.service.BudgetService;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.service.DepartmentService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "budgets", layout = MainLayout.class)
@PageTitle("Budgets | Purchase Requisition System")
@Authenticated
public class BudgetView extends ViewWithDialog {

	private final BudgetService budgetService;
	private final Grid<Budget> grid = new Grid<>();
	private final TextField searchField = new TextField();

	private Binder<@NonNull Budget> binder = new BeanValidationBinder<>(Budget.class);

	private ComboBox<Department> department = new ComboBox<>("Select Department");
	private ComboBox<Integer> fiscalYear = new ComboBox<>("Fiscal Year");
	private TextField name = new TextField("Budget Name");
	private ComboBox<BudgetType> type = new ComboBox<>("Budget Type");
	private BigDecimalField allocated = new BigDecimalField("Annual Allocation (Total)");

	public BudgetView(BudgetService budgetService, DepartmentService departmentService) {
		super();
		this.budgetService = budgetService;

		allocated.setPrefixComponent(new Div("Rp."));
		// Populasi item untuk ComboBox
		LocalDate now = LocalDate.now(ZoneId.systemDefault());
		List<Integer> selectableYears = IntStream.range(now.getYear() - 20, now.getYear() + 1).boxed().toList();
		fiscalYear.setItems(selectableYears);

		department.setItemLabelGenerator(d -> d.name != null ? d.name : "");
		department.setItems(departmentService.findAll(null, null)); // Isi items Department jika ada

		type.setItems(BudgetType.values());

		configureValidation();

		addHeader();
		addToolbar();
		configureGrid();
	}

	private void configureValidation() {
		binder.bind(department, "department");
		binder.bind(fiscalYear, "fiscalYear");
		binder.bind(name, "name");
		binder.bind(type, "type");
		binder.bind(allocated, "allocated");
	}

	@Override
	public void addHeader() {
		H2 title = new H2("Budget Planning & Management");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Manage budget items, and allocation yearly.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search products by name or SKU...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.addValueChangeListener(e -> refreshData());

		Button addBtn = new Button("Add Budget", VaadinIcon.PLUS.create(), e -> openDialog(new Budget()));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, searchField);

		add(toolbar);
	}

	@Override
	public void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(b -> b.id != null ? b.id : "-").setHeader("Budget No").setAutoWidth(true);
		grid.addColumn(b -> b.department != null && b.department.name != null ? b.department.name : "-")
				.setHeader("Department");
		grid.addColumn(b -> b.fiscalYear != null ? b.fiscalYear : "").setHeader("Year").setAutoWidth(true);
		grid.addColumn(b -> b.allocated != null ? b.allocated : "").setHeader("Amount Allocated (Year)")
				.setAutoWidth(true);

		grid.addComponentColumn(b -> {
			Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDialog(b));
			editBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			return editBtn;
		}).setHeader("Actions").setAutoWidth(true);

		add(grid);

		refreshData();
	}

	@Override
	public void refreshData() {
		var budget = new Budget();
		grid.setItems(budgetService.findAll(budget, Page.ofSize(100)));
	}

	@Override
	protected void buildAndShowDialog(@NonNull Object entity) throws Exception {
		Budget budget = (Budget) entity;
		binder.readBean(budget);

		Dialog dialog = new Dialog();
		dialog.setHeaderTitle(budget.id == null ? "Add Budget" : "Edit Budget");
		dialog.setWidth("500px");

		FormLayout form = new FormLayout();
		form.add(department, fiscalYear, name, type, allocated);

		Button saveBtn = new Button("Save", e -> {
			try {
				if (binder.writeBeanIfValid(budget)) {
					budgetService.save(budget);
					Notification.show("Budget saved successfully", 3000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					dialog.close();
					refreshData();
				} else {
					Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}
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

	@Override
	public void confirmDelete(Object entity) {
		throw new UnsupportedOperationException("Unimplemented method 'confirmDelete'");
	}
}