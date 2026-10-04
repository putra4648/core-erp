package id.putra.core_erp.modules.procurement.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import id.putra.core_erp.modules.procurement.service.RequisitionService;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.security.Authenticated;

@Route(value = "requisitions", layout = MainLayout.class)
@PageTitle("Purchase Requisitions | PR System")
@Authenticated
public class RequisitionListView extends VerticalLayout {

    private final RequisitionService requisitionService;
    private final RequisitionRepo requisitionRepo;
    private final DepartmentRepo departmentRepo;

    private final TextField searchField = new TextField();
    private final ComboBox<String> statusFilter = new ComboBox<>();
    private final Grid<Requisition> grid = new Grid<>();
    private final List<Requisition> reqList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public RequisitionListView(RequisitionService requisitionService,
            RequisitionRepo requisitionRepo,
            DepartmentRepo departmentRepo) {
        this.requisitionService = requisitionService;
        this.requisitionRepo = requisitionRepo;
        this.departmentRepo = departmentRepo;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 header = new H2("Purchase Requisitions");
        header.addClassNames(LumoUtility.FontSize.XLARGE);

        Button newPrBtn = new Button("Create Requisition", VaadinIcon.PLUS.create(),
                e -> UI.getCurrent().navigate(RequisitionFormView.class));
        newPrBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        searchField.setPlaceholder("Search PR by Title / ID...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("280px");
        searchField.addValueChangeListener(e -> refreshGrid());

        statusFilter.setItems("ALL", "DRAFT", "WAITING_APPROVAL", "APPROVED", "REJECT", "PO_CREATED");
        statusFilter.setValue("ALL");
        statusFilter.setWidth("180px");
        statusFilter.addValueChangeListener(e -> refreshGrid());

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshGrid());

        HorizontalLayout toolbar = new HorizontalLayout(searchField, statusFilter, newPrBtn, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        configureGrid();

        add(header, toolbar, grid);
        refreshGrid();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(r -> r.id != null ? r.id : "-").setHeader("PR Number").setAutoWidth(true);
        grid.addColumn(r -> r.getTitle() != null ? r.getTitle() : "-").setHeader("Title").setFlexGrow(2);
        grid.addColumn(r -> r.getDepartment() != null ? r.getDepartment().getName() : "-").setHeader("Department")
                .setFlexGrow(1);
        grid.addColumn(r -> r.getDueDate() != null ? dateFormat.format(r.getDueDate()) : "-").setHeader("Due Date")
                .setAutoWidth(true);

        grid.addComponentColumn(r -> {
            Span badge = new Span(r.getStatus() != null ? r.getStatus() : "DRAFT");
            String st = r.getStatus() != null ? r.getStatus().toUpperCase() : "";
            if (st.contains("APPROV") || st.contains("PO_")) {
                badge.getElement().getThemeList().add("badge success");
            } else if (st.contains("REJECT")) {
                badge.getElement().getThemeList().add("badge error");
            } else if (st.contains("WAIT")) {
                badge.getElement().getThemeList().add("badge warning");
            } else {
                badge.getElement().getThemeList().add("badge");
            }
            return badge;
        }).setHeader("Status").setAutoWidth(true);

        grid.addColumn(r -> {
            if (r.getNextApprovals() != null && !r.getNextApprovals().isEmpty()) {
                return String.join(", ", r.getNextApprovals());
            }
            return "-";
        }).setHeader("Next Approver").setFlexGrow(1);

        grid.addComponentColumn(r -> {
            Button viewBtn = new Button("View / Edit", VaadinIcon.EYE.create(),
                    e -> UI.getCurrent().navigate(RequisitionFormView.class, r.id.toString()));
            viewBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);

            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> {
                if ("DRAFT".equalsIgnoreCase(r.getStatus()) || "REJECT".equalsIgnoreCase(r.getStatus())) {
                    requisitionRepo.deleteById(r.id);
                    Notification.show("Requisition deleted", 2000, Notification.Position.TOP_CENTER);
                    refreshGrid();
                } else {
                    Notification.show("Cannot delete submitted requisition", 2000, Notification.Position.TOP_CENTER);
                }
            });
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);

            return new HorizontalLayout(viewBtn, deleteBtn);
        }).setHeader("Actions").setAutoWidth(true);
    }

    private void refreshGrid() {
        try {
            Requisition params = new Requisition();

            String selectedStatus = statusFilter.getValue();
            if ("ALL".equalsIgnoreCase(selectedStatus) || selectedStatus == null) {
                params.setStatus("");
            } else {
                params.setStatus(selectedStatus);
            }

            // List<String> deptIds = new ArrayList<>();
            // var currentUser = securityService.getCurrentUser();
            // if (currentUser.isPresent() && currentUser.get().getDepartment() != null) {
            // deptIds.add(currentUser.get().getDepartment().getCode());
            // if (currentUser.get().getOtherDepts() != null) {
            // deptIds.addAll(currentUser.get().getOtherDepts());
            // }
            // } else {
            // departmentRepo.findAll().list().forEach(d -> deptIds.add(d.getCode()));
            // }

            List<Requisition> res = requisitionService.findAll(params, null);
            reqList.clear();
            if (res != null) {
                reqList.addAll(res);
            }
            grid.setItems(reqList);
        } catch (Exception ex) {
            Notification.show("Error loading requisitions: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
