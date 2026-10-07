package id.putra.core_erp.modules.procurement.views;

import java.text.SimpleDateFormat;
import com.vaadin.flow.component.button.Button;
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

import id.putra.core_erp.modules.procurement.model.Transfer;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.security.Authenticated;

@Route(value = "transfers", layout = MainLayout.class)
@PageTitle("Transfers | Purchase Requisition System")
@Authenticated
public class TransferView extends ViewWithDialog {

	private final TextField searchField = new TextField();
	private final Grid<Transfer> grid = new Grid<>();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public TransferView() {
		super();

		addHeader();
		addToolbar();
		configureGrid();

	}

	@Override
	public void addHeader() {
		H2 header = new H2("Transfer Requests");
		header.addClassNames(LumoUtility.FontSize.XLARGE);
		add(header);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search Transfer ID or Title...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.setWidth("280px");
		searchField.addValueChangeListener(e -> refreshData());

		Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, refreshBtn);
		toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

		add(toolbar);
	}

	@Override
	public void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(t -> t.id != null ? t.id : "-").setHeader("Transfer Number").setAutoWidth(true);
		grid.addColumn(t -> t.title != null ? t.title : "-").setHeader("Title").setFlexGrow(2);
		grid.addColumn(t -> t.department != null ? t.department.name : "-").setHeader("Department")
				.setFlexGrow(1);
		grid.addColumn(t -> t.dueDate != null ? dateFormat.format(t.dueDate) : "-").setHeader("Due Date")
				.setAutoWidth(true);

		grid.addComponentColumn(t -> {
			Span badge = new Span(t.status != null ? t.status : "DRAFT");
			if ("APPROVED".equalsIgnoreCase(t.status) || "COMPLETED".equalsIgnoreCase(t.status)) {
				badge.getElement().getThemeList().add("badge success");
			} else if ("REJECT".equalsIgnoreCase(t.status)) {
				badge.getElement().getThemeList().add("badge error");
			} else {
				badge.getElement().getThemeList().add("badge");
			}
			return badge;
		}).setHeader("Status").setAutoWidth(true);

		add(grid);

		refreshData();
	}

	@Override
	public void refreshData() {
		try {
			Transfer params = new Transfer();

		} catch (Exception ex) {
			Notification.show("Failed to load transfers: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
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
