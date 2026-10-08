package id.putra.core_erp.modules.procurement.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.procurement.model.Quotation;
import id.putra.core_erp.modules.procurement.service.QuotationService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.security.Authenticated;

@Route(value = "quotation", layout = MainLayout.class)
@PageTitle("Quotation | PR System")
@Authenticated
public class QuotationView extends ViewWithDialog {

	private final QuotationService quotationService;

	private final TextField searchField = new TextField();
	private final Grid<Quotation> grid = new Grid<>();
	private final List<Quotation> rfqList = new ArrayList<>();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public QuotationView(QuotationService rfqService) {
		super();
		this.quotationService = rfqService;

		addHeader();
		addToolbar();
		configureGrid();

	}

	@Override
	public void addHeader() {
		H2 header = new H2("Quotation");
		header.addClassNames(LumoUtility.FontSize.XLARGE);
		Paragraph desc = new Paragraph("List of all quotations.");
		desc.addClassNames("text-secondary", "m-0");
		add(header, desc);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search RFQ by ID or Note...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.setWidth("280px");
		searchField.addValueChangeListener(e -> refreshData());

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		Button addBtn = new Button("Add Request For Quotation", VaadinIcon.PLUS.create(),
				e -> openDialog(new Budget()));
		addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
		toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

		add(toolbar);
	}

	@Override
	public void configureGrid() {

		grid.setSizeFull();
		grid.addColumn(r -> r.id != null ? r.id : "-").setHeader("RFQ Number").setAutoWidth(true);
		grid.addColumn(r -> r.dueDate != null ? dateFormat.format(r.dueDate) : "-").setHeader("Due Date")
				.setAutoWidth(true);
		grid.addColumn(r -> r.note != null ? r.note : "-").setHeader("Notes").setFlexGrow(2);
		grid.addColumn(r -> r.suppliers != null ? r.suppliers.size() + " suppliers" : "0 suppliers")
				.setHeader("Suppliers Quoted").setAutoWidth(true);

		add(grid);

		refreshData();

	}

	@Override
	public void refreshData() {
		try {
			Quotation params = new Quotation();

			List<Quotation> res = quotationService.findAll(params);
			rfqList.clear();
			if (res != null) {
				rfqList.addAll(res);
			}
			grid.setItems(rfqList);
		} catch (Exception ex) {
			Notification.show("Failed to load RFQs: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	@Override
	protected void buildAndShowDialog(Object entity) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'openDialog'");
	}

	@Override
	public void confirmDelete(Object entity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'confirmDelete'");
	}
}
