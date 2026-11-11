package id.putra.core_erp.modules.setting.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.approval.service.ApprovalWorkflowService;
import id.putra.core_erp.modules.department.service.DepartmentService;
import id.putra.core_erp.modules.parameter.service.ParameterLookupService;
import id.putra.core_erp.modules.setting.views.components.ApprovalWorkflowComponent;
import id.putra.core_erp.modules.setting.views.components.DepartmentComponent;
import id.putra.core_erp.ui.MainLayout;
import io.quarkus.security.Authenticated;

@Route(value = "settings", layout = MainLayout.class)
@PageTitle("Settings")
@Authenticated
public class SettingsView extends VerticalLayout {

	private final TabSheet tabs = new TabSheet();

	public SettingsView(ParameterLookupService parameterLookupService, DepartmentService departmentService,
			ApprovalWorkflowService approvalWorkflowService) {

		setSizeFull();
		setPadding(true);
		setSpacing(true);

		addHeader();
		initTab(parameterLookupService, departmentService, approvalWorkflowService);
	}

	private void initTab(ParameterLookupService parameterLookupService, DepartmentService departmentService,
			ApprovalWorkflowService approvalWorkflowService) {
		tabs.setSizeFull();
		// tabs.add("System Parameter", new
		// ParameterLookupComponent(parameterLookupService));
		tabs.add("Department", new DepartmentComponent(departmentService));
		tabs.add("Approval Workflow", new ApprovalWorkflowComponent(approvalWorkflowService));

		add(tabs);
	}

	private void addHeader() {
		H2 title = new H2("System Settings");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Configure application parameters, dark mode, and system defaults.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

}
