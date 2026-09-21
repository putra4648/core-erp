package id.putra.core_erp.modules.procurement.views;

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

import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.modules.procurement.model.Transfer;
import id.putra.core_erp.modules.procurement.service.TransferService;
import id.putra.core_erp.shared.ui.MainLayout;
import jakarta.annotation.security.PermitAll;

@Route(value = "transfers", layout = MainLayout.class)
@PageTitle("Transfers | Purchase Requisition System")
@PermitAll
public class TransferView extends VerticalLayout {

    private final TransferService transferService;
    private final DepartmentRepo departmentRepo;

    private final TextField searchField = new TextField();
    private final Grid<Transfer> grid = new Grid<>();
    private final List<Transfer> transferList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public TransferView(TransferService transferService, DepartmentRepo departmentRepo) {
        this.transferService = transferService;
        this.departmentRepo = departmentRepo;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 header = new H2("Transfer Requests");
        header.addClassNames(LumoUtility.FontSize.XLARGE);

        searchField.setPlaceholder("Search Transfer ID or Title...");
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
        grid.addColumn(t -> t.getId() != null ? t.getId() : "-").setHeader("Transfer Number").setAutoWidth(true);
        grid.addColumn(t -> t.getTitle() != null ? t.getTitle() : "-").setHeader("Title").setFlexGrow(2);
        grid.addColumn(t -> t.getDepartment() != null ? t.getDepartment().getDeptName() : "-").setHeader("Department")
                .setFlexGrow(1);
        grid.addColumn(t -> t.getDueDate() != null ? dateFormat.format(t.getDueDate()) : "-").setHeader("Due Date")
                .setAutoWidth(true);

        grid.addComponentColumn(t -> {
            Span badge = new Span(t.getStatus() != null ? t.getStatus() : "DRAFT");
            if ("APPROVED".equalsIgnoreCase(t.getStatus()) || "COMPLETED".equalsIgnoreCase(t.getStatus())) {
                badge.getElement().getThemeList().add("badge success");
            } else if ("REJECT".equalsIgnoreCase(t.getStatus())) {
                badge.getElement().getThemeList().add("badge error");
            } else {
                badge.getElement().getThemeList().add("badge");
            }
            return badge;
        }).setHeader("Status").setAutoWidth(true);
    }

    private void refreshGrid() {
        try {
            Transfer params = new Transfer();

            List<String> deptIds = new ArrayList<>();
            departmentRepo.findAll().list().forEach(d -> deptIds.add(d.getDeptNumber()));

            List<Transfer> res = transferService.findAll(params);
            transferList.clear();
            if (res != null) {
                transferList.addAll(res);
            }
            grid.setItems(transferList);
        } catch (Exception ex) {
            Notification.show("Failed to load transfers: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
