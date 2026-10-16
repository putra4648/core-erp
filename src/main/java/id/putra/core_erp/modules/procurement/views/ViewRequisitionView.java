package id.putra.core_erp.modules.procurement.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import id.putra.core_erp.modules.procurement.model.RequisitionItem;
import id.putra.core_erp.modules.procurement.service.RequisitionService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "requisition", layout = MainLayout.class)
@PageTitle("Detail Requisition | PR System")
@Authenticated
public class ViewRequisitionView extends VerticalLayout implements HasUrlParameter<Long> {

    private final RequisitionService requisitionService;
    private final Grid<RequisitionItem> itemGrid = new Grid<>();

    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        var list = requisitionService.findAllItem(parameter, Page.ofSize(100));
        System.out.println("SIZE " + list.size());
        itemGrid.setItems(list);
    }

    public ViewRequisitionView(RequisitionService requisitionService) {
        this.requisitionService = requisitionService;

        Button backBtn = new Button("Back", VaadinIcon.ARROW_LEFT.create());
        backBtn.addClickListener(e -> e.getUI().navigate(RequisitionView.class));
        backBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout topBar = new HorizontalLayout(backBtn, new H2("Requisition Details"));
        topBar.setAlignItems(FlexComponent.Alignment.CENTER);

        add(topBar);

        configureGrid();
    }

    private void configureGrid() {
        itemGrid.addColumn(s -> s.name)
                .setHeader("Name")
                .setKey("name")
                .setAutoWidth(true);

        itemGrid.addColumn(s -> s.quantity)
                .setHeader("Quantity")
                .setKey("quantity")
                .setAutoWidth(true);

        itemGrid.addColumn(s -> s.unitPrice)
                .setHeader("Unit Price")
                .setKey("unitPrice")
                .setAutoWidth(true);

        add(itemGrid);

    }

}
