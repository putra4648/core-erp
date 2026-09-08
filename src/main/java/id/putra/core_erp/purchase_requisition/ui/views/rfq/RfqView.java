package id.putra.core_erp.purchase_requisition.ui.views.rfq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
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

import id.putra.core_erp.purchase_requisition.model.RFQ;
import id.putra.core_erp.purchase_requisition.service.RFQService;
import id.putra.core_erp.shared.ui.MainLayout;
import jakarta.annotation.security.PermitAll;

@Route(value = "rfq", layout = MainLayout.class)
@PageTitle("Request For Quotations | PR System")
@PermitAll
public class RfqView extends VerticalLayout {

    private final RFQService rfqService;

    private final TextField searchField = new TextField();
    private final Grid<RFQ> grid = new Grid<>();
    private final List<RFQ> rfqList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public RfqView(RFQService rfqService) {
        this.rfqService = rfqService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 header = new H2("Request For Quotations (RFQ)");
        header.addClassNames(LumoUtility.FontSize.XLARGE);

        searchField.setPlaceholder("Search RFQ by ID or Note...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("280px");
        searchField.addValueChangeListener(e -> refreshGrid());

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshGrid());

        HorizontalLayout toolbar = new HorizontalLayout(searchField, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        configureGrid();

        add(header, toolbar, grid);
        refreshGrid();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(r -> r.getId() != null ? r.getId() : "-").setHeader("RFQ Number").setAutoWidth(true);
        grid.addColumn(r -> r.getDueDate() != null ? dateFormat.format(r.getDueDate()) : "-").setHeader("Due Date")
                .setAutoWidth(true);
        grid.addColumn(r -> r.getNote() != null ? r.getNote() : "-").setHeader("Notes").setFlexGrow(2);
        grid.addColumn(r -> r.getSuppliers() != null ? r.getSuppliers().size() + " suppliers" : "0 suppliers")
                .setHeader("Suppliers Quoted").setAutoWidth(true);

        grid.addComponentColumn(r -> {
            Span badge = new Span(r.getStatus() != null ? r.getStatus() : "ACTIVE");
            if ("COMPLETED".equalsIgnoreCase(r.getStatus()) || "APPROVED".equalsIgnoreCase(r.getStatus())) {
                badge.getElement().getThemeList().add("badge success");
            } else {
                badge.getElement().getThemeList().add("badge");
            }
            return badge;
        }).setHeader("Status").setAutoWidth(true);
    }

    private void refreshGrid() {
        try {
            RFQ params = new RFQ();

            List<RFQ> res = rfqService.findAll();
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
}
