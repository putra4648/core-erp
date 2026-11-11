package id.putra.core_erp.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.ui.theme.ThemeController;
import id.putra.core_erp.ui.theme.ThemeToggleButton;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import jakarta.inject.Inject;

@Authenticated
public class MainLayout extends AppLayout {
	@Inject
	SecurityIdentity securityIdentity;

	public MainLayout(SecurityIdentity securityIdentity) {
		ThemeController.applyStored();

		setPrimarySection(Section.DRAWER);

		var principal = (DefaultJWTCallerPrincipal) securityIdentity.getPrincipal();
		createHeader(principal);
		createDrawer(principal);
	}

	private void createHeader(DefaultJWTCallerPrincipal principal) {
		DrawerToggle toggle = new DrawerToggle();
		toggle.setAriaLabel("Menu toggle");

		H2 title = new H2("CORE ERP");
		title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);
		title.addClassName(LumoUtility.FontWeight.BOLD);
		title.addClassName("app-brand");
		title.getStyle().setMarginRight("auto");

		ThemeToggleButton themeToggle = new ThemeToggleButton();

		Avatar avatar = new Avatar(principal.getName());
		if (principal.getClaim("picture") != null && !principal.getClaim("picture").toString().isBlank()) {
			avatar.setImage(principal.getClaim("picture"));
		}
		avatar.addClassNames(LumoUtility.Margin.Right.SMALL);

		MenuBar userMenu = new MenuBar();
		userMenu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
		MenuItem userMenuItem = userMenu.addItem(avatar);
		SubMenu subMenu = userMenuItem.getSubMenu();

		subMenu.addItem("Profile", e -> {
			System.out.println("Principal: " + principal.getClaimNames());
			System.out.println("Security Identity: " + securityIdentity.getRoles());
		});
		subMenu.addItem("Settings", e -> UI.getCurrent().navigate("settings"));
		subMenu.addItem("Logout", e -> UI.getCurrent().getPage().setLocation("/logout"));

		HorizontalLayout header = new HorizontalLayout(toggle, title, themeToggle, userMenu);
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		header.setWidthFull();
		header.addClassNames(LumoUtility.Padding.Vertical.SMALL, LumoUtility.Padding.Horizontal.MEDIUM);

		addToNavbar(header);
	}

	private void createDrawer(DefaultJWTCallerPrincipal principal) {
		SideNav mainNav = new SideNav();
		mainNav.setLabel("Main");
		mainNav.addItem(new SideNavItem("Dashboard", "", VaadinIcon.DASHBOARD.create()));
		mainNav.setWidthFull();

		SideNav warehouseNav = new SideNav();
		warehouseNav.setLabel("Warehouse");
		warehouseNav.addItem(new SideNavItem("Receiving", "receiving", VaadinIcon.INBOX.create()));
		warehouseNav.addItem(new SideNavItem("Inventory Stock", "inventory", VaadinIcon.PACKAGE.create()));
		warehouseNav.addItem(new SideNavItem("Outbound & Shipping", "outbound", VaadinIcon.UPLOAD_ALT.create()));
		warehouseNav.setWidthFull();

		SideNav procNav = new SideNav();
		procNav.setLabel("Procurement");
		procNav.addItem(
				new SideNavItem("Dashboard", "requisition/dashboard", VaadinIcon.CLIPBOARD_CHECK.create()));
		procNav.addItem(
				new SideNavItem("Requisition", "requisition", VaadinIcon.CLIPBOARD_CHECK.create()));
		procNav.addItem(new SideNavItem("Quotation", "quotation", VaadinIcon.MONEY.create()));
		procNav.addItem(new SideNavItem("Purchase Orders", "po", VaadinIcon.FILE_TEXT.create()));
		procNav.setWidthFull();

		SideNav masterNav = new SideNav();
		masterNav.setLabel("Master Data");
		masterNav.addItem(new SideNavItem("Products", "master/products", VaadinIcon.RECORDS.create()));
		masterNav.addItem(new SideNavItem("Suppliers", "master/suppliers", VaadinIcon.TRUCK.create()));
		masterNav.addItem(new SideNavItem("Customers", "master/customer", VaadinIcon.USERS.create()));
		masterNav.addItem(new SideNavItem("Warehouses", "master/warehouses", VaadinIcon.BUILDING.create()));
		masterNav.addItem(new SideNavItem("Budget Planning", "budgets", VaadinIcon.CHART_LINE.create()));

		masterNav.setWidthFull();

		SideNav reportsNav = new SideNav();
		reportsNav.setLabel("Reports & Analytics");
		reportsNav.addItem(new SideNavItem("WMS Reports", "reports", VaadinIcon.PIE_CHART.create()));
		reportsNav.setWidthFull();

		SideNav adminNav = new SideNav();
		adminNav.setLabel("Administration");
		adminNav.addItem(new SideNavItem("System Settings", "settings", VaadinIcon.COG.create()));
		// adminNav.addItem(new SideNavItem("Help & Support", "help",
		// VaadinIcon.QUESTION_CIRCLE.create()));
		adminNav.setWidthFull();

		VerticalLayout navContainer = new VerticalLayout(mainNav, warehouseNav, procNav, masterNav, reportsNav,
				adminNav);
		navContainer.setPadding(true);
		navContainer.setSpacing(true);
		navContainer.setWidthFull();

		Scroller scroller = new Scroller(navContainer);
		scroller.addClassNames(LumoUtility.Padding.SMALL);

		addToDrawer(scroller);
	}
}
