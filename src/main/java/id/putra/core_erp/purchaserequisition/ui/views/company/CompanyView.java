package id.putra.core_erp.purchaserequisition.ui.views.company;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.purchaserequisition.dto.CompanyDto;
import id.putra.core_erp.purchaserequisition.service.CompanyService;
import id.putra.core_erp.shared.ui.MainLayout;
import jakarta.annotation.security.PermitAll;

@Route(value = "company", layout = MainLayout.class)
@PageTitle("Company Profile | Purchase Requisition System")
@PermitAll
public class CompanyView extends VerticalLayout {

    private final CompanyService companyService;

    private Integer companyId = 1;
    private final TextField nameField = new TextField("Company Name");
    private final TextField telpField = new TextField("Telephone");
    private final TextField npwpField = new TextField("NPWP");
    private final TextField accBankField = new TextField("Bank Account");
    private final DatePicker endYearPicker = new DatePicker("Financial End Year");
    private final TextField curr1Field = new TextField("Primary Currency");
    private final TextField curr2Field = new TextField("Secondary Currency");
    private final TextField curr3Field = new TextField("Tertiary Currency");
    private final Checkbox autoCloseOrderBox = new Checkbox("Auto Close Order");
    private final Checkbox registerApprovalBox = new Checkbox("Require Register Approval");
    private final Button saveButton = new Button("Save Changes");

    public CompanyView(CompanyService companyService) {
        this.companyService = companyService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 header = new H2("Company Profile & Settings");
        header.addClassNames(LumoUtility.FontSize.XLARGE);

        VerticalLayout card = new VerticalLayout();
        card.setMaxWidth("800px");
        card.setPadding(true);
        card.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "12px")
            .set("box-shadow", "0 1px 3px 0 rgba(0, 0, 0, 0.1)")
            .set("border", "1px solid #e2e8f0");

        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );

        form.add(nameField, telpField, npwpField, accBankField, endYearPicker, curr1Field, curr2Field, curr3Field);
        form.setColspan(nameField, 2);

        HorizontalLayout checkboxRow = new HorizontalLayout(autoCloseOrderBox, registerApprovalBox);
        checkboxRow.setSpacing(true);
        checkboxRow.addClassNames(LumoUtility.Margin.Vertical.MEDIUM);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> handleSave());

        card.add(form, checkboxRow, saveButton);
        add(header, card);

        loadCompanyData();
    }

    private void loadCompanyData() {
        try {
            CompanyDto dto = companyService.getProfile();
            if (dto != null) {
                companyId = dto.getId() != null ? dto.getId() : 1;
                nameField.setValue(dto.getName() != null ? dto.getName() : "");
                telpField.setValue(dto.getTelp() != null ? dto.getTelp() : "");
                npwpField.setValue(dto.getNpwp() != null ? dto.getNpwp() : "");
                accBankField.setValue(dto.getAccountBank() != null ? dto.getAccountBank() : "");
                if (dto.getFinancialEndYear() != null && !dto.getFinancialEndYear().isBlank()) {
                    try {
                        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getFinancialEndYear());
                        endYearPicker.setValue(d.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                    } catch (Exception ignored) {}
                }
                curr1Field.setValue(dto.getCurr1() != null ? dto.getCurr1() : "IDR");
                curr2Field.setValue(dto.getCurr2() != null ? dto.getCurr2() : "USD");
                curr3Field.setValue(dto.getCurr3() != null ? dto.getCurr3() : "EUR");
                autoCloseOrderBox.setValue(Boolean.TRUE.equals(dto.getIsAutoCloseOrder()));
                registerApprovalBox.setValue(Boolean.TRUE.equals(dto.getIsRegisterApproval()));
            }
        } catch (Exception ex) {
            // handle
        }
    }

    private void handleSave() {
        CompanyDto dto = new CompanyDto();
        dto.setId(companyId);
        dto.setName(nameField.getValue());
        dto.setTelp(telpField.getValue());
        dto.setNpwp(npwpField.getValue());
        dto.setAccountBank(accBankField.getValue());
        if (endYearPicker.getValue() != null) {
            dto.setFinancialEndYear(endYearPicker.getValue().toString());
        } else {
            dto.setFinancialEndYear(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        dto.setCurr1(curr1Field.getValue());
        dto.setCurr2(curr2Field.getValue());
        dto.setCurr3(curr3Field.getValue());
        dto.setIsAutoCloseOrder(autoCloseOrderBox.getValue());
        dto.setIsRegisterApproval(registerApprovalBox.getValue());

        try {
            companyService.handleCompany(dto, null);
            Notification.show("Company profile updated successfully", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception ex) {
            Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}


