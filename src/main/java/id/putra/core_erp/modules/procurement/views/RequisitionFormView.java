package id.putra.core_erp.modules.procurement.views;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.modules.budget.repository.BudgetDetailRepo;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.modules.approval.enums.ApprovalEnum;
import id.putra.core_erp.modules.procurement.enums.RequisitionEnum;
import id.putra.core_erp.modules.procurement.model.Approval;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.model.RequisitionDetail;
import id.putra.core_erp.modules.procurement.repository.ItemRepo;
import id.putra.core_erp.modules.procurement.service.RequisitionService;
import id.putra.core_erp.shared.ui.MainLayout;
import jakarta.annotation.security.PermitAll;

@Route(value = "requisitions/form", layout = MainLayout.class)
@PageTitle("Requisition Form | PR System")
@PermitAll
public class RequisitionFormView extends VerticalLayout implements HasUrlParameter<String> {

    private final RequisitionService requisitionService;
    private final DepartmentRepo departmentRepo;
    private final BudgetDetailRepo budgetDetailRepo;
    private final ItemRepo itemRepo;

    private Long requisitionId = null;
    private String currentStatus = "DRAFT";

    private final H2 titleHeader = new H2("Create Purchase Requisition");
    private final TextField titleField = new TextField("PR Title");
    private final ComboBox<Department> deptCombo = new ComboBox<>("Department");
    private final DatePicker dueDatePicker = new DatePicker("Due Date");
    private final TextField budgetDetailIdField = new TextField("Budget Detail Reference ID");
    private final TextArea noteField = new TextArea("Requisition Notes / Justification");

    private final Grid<RequisitionDetail> itemsGrid = new Grid<>();
    private final List<RequisitionDetail> lineItems = new ArrayList<>();
    private final List<Department> departmentList = new ArrayList<>();
    private final Span grandTotalSpan = new Span("Total: IDR 0");

    private final Button saveDraftBtn = new Button("Save Draft");
    private final Button submitBtn = new Button("Submit for Approval");
    private final Button approveBtn = new Button("Approve");
    private final Button rejectBtn = new Button("Reject");
    private final Button backBtn = new Button("Back to Requisitions", VaadinIcon.ARROW_LEFT.create(),
            e -> UI.getCurrent().navigate(RequisitionListView.class));

    public RequisitionFormView(RequisitionService requisitionService,
            DepartmentRepo departmentRepo,
            BudgetDetailRepo budgetDetailRepo,
            ItemRepo itemRepo) {
        this.requisitionService = requisitionService;
        this.departmentRepo = departmentRepo;
        this.budgetDetailRepo = budgetDetailRepo;
        this.itemRepo = itemRepo;

        departmentRepo.findAll().list().forEach(departmentList::add);

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        HorizontalLayout topBar = new HorizontalLayout(backBtn);
        topBar.setAlignItems(FlexComponent.Alignment.CENTER);

        add(topBar, titleHeader, createFormSection(), createItemsSection(), createActionButtons());
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
            dueDatePicker.setValue(LocalDate.now().plusDays(14));
            // securityService.().ifPresent(u -> {
            // if (u.getDepartment() != null) {
            // deptCombo.setValue(u.getDepartment());
            // }
            // });
        }
    }

    private VerticalLayout createFormSection() {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(true);
        card.getStyle()
                .set("background", "#ffffff")
                .set("border-radius", "12px")
                .set("border", "1px solid #e2e8f0");

        FormLayout form = new FormLayout();
        titleField.setRequired(true);
        titleField.setWidthFull();

        deptCombo.setItemLabelGenerator(d -> d.getDeptNumber() + " - " + d.getDeptName());
        deptCombo.setItems(departmentList);
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
        card.getStyle()
                .set("background", "#ffffff")
                .set("border-radius", "12px")
                .set("border", "1px solid #e2e8f0");

        H3 itemsTitle = new H3("Line Items");
        itemsTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.SEMIBOLD);

        Button addItemBtn = new Button("Add Item", VaadinIcon.PLUS.create(), e -> openAddItemDialog());
        addItemBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);

        HorizontalLayout itemsBar = new HorizontalLayout(itemsTitle, addItemBtn);
        itemsBar.setWidthFull();
        itemsBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        itemsBar.setAlignItems(FlexComponent.Alignment.CENTER);

        itemsGrid.setHeight("250px");
        itemsGrid.addColumn(d -> d.getName() != null ? d.getName() : "-").setHeader("Item Name").setFlexGrow(2);
        itemsGrid.addColumn(d -> d.getItemNumber() != null ? d.getItemNumber() : "-").setHeader("Item Code")
                .setAutoWidth(true);
        itemsGrid.addColumn(d -> d.getUom() != null ? d.getUom() : "PCS").setHeader("UOM").setAutoWidth(true);
        itemsGrid.addColumn(d -> d.getQty() != null ? d.getQty().toString() : "1").setHeader("Qty").setAutoWidth(true);
        itemsGrid.addColumn(d -> d.getUnitPrice() != null ? String.format("%,d", d.getUnitPrice()) : "0")
                .setHeader("Unit Price").setFlexGrow(1);
        itemsGrid.addColumn(d -> d.getSubtotal() != null ? String.format("%,d", d.getSubtotal()) : "0")
                .setHeader("Subtotal").setFlexGrow(1);

        itemsGrid.addComponentColumn(item -> {
            Button delBtn = new Button(VaadinIcon.TRASH.create(), e -> {
                lineItems.remove(item);
                itemsGrid.setItems(lineItems);
                recalculateTotal();
            });
            delBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            return delBtn;
        }).setHeader("Action").setAutoWidth(true);

        grandTotalSpan.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD,
                LumoUtility.Margin.Left.AUTO);

        HorizontalLayout footer = new HorizontalLayout(grandTotalSpan);
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        card.add(itemsBar, itemsGrid, footer);
        return card;
    }

    private HorizontalLayout createActionButtons() {
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);
        actions.addClassNames(LumoUtility.Margin.Top.MEDIUM);

        saveDraftBtn.addClickListener(e -> saveRequisition(RequisitionEnum.DRAFT.getValue()));

        submitBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitBtn.addClickListener(e -> saveRequisition(RequisitionEnum.SEND.getValue()));

        approveBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        approveBtn.addClickListener(e -> handleApproval(RequisitionEnum.APPROVED.getValue()));
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

            RequisitionDetail item = new RequisitionDetail();
            item.setId(String.valueOf(lineItems.size() + 1));
            item.setName(nameField.getValue());
            item.setItemNumber(itemNumberField.getValue());
            item.setUom(uomField.getValue());
            item.setDescription(descField.getValue());
            item.setSpecFiles(new ArrayList<>());

            try {
                item.setQty(new BigInteger(qtyField.getValue().replaceAll("[^0-9]", "")));
            } catch (Exception ex) {
                item.setQty(BigInteger.ONE);
            }

            try {
                BigDecimal price = new BigDecimal(unitPriceField.getValue().replaceAll("[^0-9]", ""));
                item.setUnitPrice(price);
                item.setSubtotal(price.multiply(new BigDecimal(item.getQty())));
            } catch (Exception ex) {
                item.setUnitPrice(BigDecimal.ZERO);
                item.setSubtotal(BigDecimal.ZERO);
            }

            lineItems.add(item);
            itemsGrid.setItems(lineItems);
            recalculateTotal();
            dialog.close();
        });
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", ev -> dialog.close());

        dialog.add(form);
        dialog.getFooter().add(cancelBtn, addBtn);
        dialog.open();
    }

    private void recalculateTotal() {
        BigDecimal total = lineItems.stream()
                .map(d -> d.getSubtotal() != null ? d.getSubtotal() : BigDecimal.ZERO)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        grandTotalSpan.setText("Total: IDR " + String.format("%,d", total));
    }

    private void loadRequisition(Long id) {

        try {
            Requisition r = requisitionService.findById(id);
            if (r != null && r.getId() != null) {
                this.currentStatus = r.getStatus() != null ? r.getStatus() : "DRAFT";
                titleField.setValue(r.getTitle() != null ? r.getTitle() : "");
                noteField.setValue(r.getNote() != null ? r.getNote() : "");
                if (r.getBudget() != null) {
                    budgetDetailIdField.setValue(String.valueOf(r.getBudget().getId()));
                }
                // if (r.getDueDate() != null) {
                // dueDatePicker.setValue(r.getDueDate());
                // }
                // if (r.getDepartment() != null && r.getDepartment().getDept_number() != null)
                // {
                // departmentRepo.findByDeptNumber(r.getDepartment().getDept_number()).ifPresent(deptCombo::setValue);
                // }

                lineItems.clear();
                if (r.getDetails() != null) {
                    lineItems.addAll(r.getDetails());
                }
                itemsGrid.setItems(lineItems);
                recalculateTotal();

                // Check approval status
                try {
                    ApprovalEnum approvalStatus = requisitionService.checkStatusApprovalIsPermanent(r);
                    if (approvalStatus == ApprovalEnum.NEED_APPROVAL) {
                        approveBtn.setVisible(true);
                        rejectBtn.setVisible(true);
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ex) {
            Notification.show("Error loading PR: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void saveRequisition(String status) {
        if (titleField.getValue().isBlank() || deptCombo.getValue() == null) {
            Notification.show("Title and Department are required", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (lineItems.isEmpty()) {
            Notification.show("Please add at least one line item", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        Requisition params = new Requisition();
        params.setId(requisitionId != null ? requisitionId : null);
        params.setTitle(titleField.getValue());
        params.setNote(noteField.getValue());
        params.setStatus(status);

        Department deptDto = new Department();
        deptDto.setDeptNumber(deptCombo.getValue().getDeptNumber());
        deptDto.setDeptName(deptCombo.getValue().getDeptName());
        params.setDepartment(deptDto);

        if (dueDatePicker.getValue() != null) {
            params.setDueDate(Date.from(dueDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            params.setDueDate(new Date());
        }

        params.setBudgetDetailId(budgetDetailIdField.getValue() != null && !budgetDetailIdField.getValue().isBlank()
                ? Long.parseLong(budgetDetailIdField.getValue())
                : 1);

        params.setDetails(lineItems);

        // build default approval list
        List<Approval> approvals = new ArrayList<>();
        if (deptCombo.getValue().getApprovals() != null) {
            for (Approval app : deptCombo.getValue().getApprovals()) {
                if (app.getUsers() != null) {
                    Approval appDto = new Approval();
                    String nrp = "";
                    appDto.setUsers(nrp);
                    appDto.setIsApprovalPermanent(false);
                    appDto.setIsBypassPermanent(false);
                    approvals.add(appDto);
                }
            }
        }
        params.setApprovals(approvals);

        if (deptCombo.getValue().getDeptPurchaser() != null) {
            Approval purchaserDto = new Approval();
            String nrp = "";
            purchaserDto.setUsers(nrp);
            purchaserDto.setIsApprovalPermanent(false);
            params.setPurchaser(purchaserDto);
        }

        try {
            requisitionService.add(params);
            Notification.show("Requisition saved successfully", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().navigate(RequisitionListView.class);
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
            saveRequisition(RequisitionEnum.REJECT.getValue());
        });
        rejectConfirm.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        Button cancel = new Button("Cancel", e -> dialog.close());

        dialog.add(rejectReason);
        dialog.getFooter().add(cancel, rejectConfirm);
        dialog.open();
    }
}
