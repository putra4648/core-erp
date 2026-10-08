package id.putra.core_erp.modules.company.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.modules.company.model.Company;
import id.putra.core_erp.modules.company.service.CompanyService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.security.Authenticated;

@Route(value = "company", layout = MainLayout.class)
@PageTitle("Company Profile | Purchase Requisition System")
@Authenticated
public class CompanyView extends ViewWithDialog {

	private final CompanyService companyService;

	private final Binder<Company> binder = new BeanValidationBinder<>(Company.class);
	private final TextField telp = new TextField("Telephone");
	private final TextField npwp = new TextField("NPWP");
	private final TextField accountBank = new TextField("Bank Account");
	private final DatePicker endYearPicker = new DatePicker("Financial End Year");
	private final ComboBox<String> currency = new ComboBox<>("Currency");
	private final Checkbox autoCloseOrderBox = new Checkbox("Auto Close Order");
	private final Checkbox registerApprovalBox = new Checkbox("Require Register Approval");
	private final Button saveButton = new Button("Save Changes");

	public CompanyView(CompanyService companyService) {
		super();
		this.companyService = companyService;

		configureValidation();

		refreshData();
	}

	private void configureValidation() {
		binder.bind(telp, "telp");
		binder.bind(npwp, "npwp");
		binder.bind(accountBank, "accountBank");
		binder.bind(endYearPicker, "endYear");
		binder.bind(currency, "currency");
	}

	@Override
	public void addHeader() {
		H2 header = new H2("Company Profile & Settings");
		header.addClassNames(LumoUtility.FontSize.XLARGE);

		add(header);
	}

	@Override
	public void addToolbar() {
		VerticalLayout card = new VerticalLayout();
		card.setMaxWidth("800px");
		card.setPadding(true);
		card.getStyle().set("background", "#ffffff").set("border-radius", "12px")
				.set("box-shadow", "0 1px 3px 0 rgba(0, 0, 0, 0.1)").set("border", "1px solid #e2e8f0");

		FormLayout form = new FormLayout();
		form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("500px", 2));

		currency.setItems("Rp", "$", "€", "¥");

		form.add(telp, npwp, accountBank, endYearPicker, currency);

		HorizontalLayout checkboxRow = new HorizontalLayout(autoCloseOrderBox, registerApprovalBox);
		checkboxRow.setSpacing(true);
		checkboxRow.addClassNames(LumoUtility.Margin.Vertical.MEDIUM);

		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickListener(e -> handleSave());

		card.add(form, checkboxRow, saveButton);
	}

	@Override
	public void refreshData() {
		try {
			Company dto = companyService.findById(1L);
			if (dto == null) {
				dto = new Company();
			}
			binder.readBean(dto);
		} catch (Exception ex) {
			// handle
		}
	}

	private void handleSave() {
		Company dto = companyService.findById(1L);
		if (dto == null) {
			dto = new Company();
		}

		try {
			if (binder.writeBeanIfValid(dto)) {
				companyService.save(dto);
				Notification.show("Company profile updated successfully", 3000, Notification.Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			} else {
				Notification.show("Please fill all required fields correctly", 4000, Notification.Position.TOP_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		} catch (Exception ex) {
			Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	@Override
	public void configureGrid() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'configureGrid'");
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
