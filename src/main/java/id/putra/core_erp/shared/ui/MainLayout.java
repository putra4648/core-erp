package id.putra.core_erp.shared.ui;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;

@AnonymousAllowed
public class MainLayout extends AppLayout {

    private final AuthenticationContext authContext;

    public MainLayout(AuthenticationContext authContext) {
        this.authContext = authContext;
        setPrimarySection(Section.DRAWER);
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        H2 title = new H2("CORE ERP");
        title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);
        title.getStyle().setMarginRight("auto");

        Button themeToggle = new Button(VaadinIcon.MOON.create(), click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();
            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
                click.getSource().setIcon(VaadinIcon.MOON.create());
            } else {
                themeList.add(Lumo.DARK);
                click.getSource().setIcon(VaadinIcon.SUN_O.create());
            }
        });
        themeToggle.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        themeToggle.setAriaLabel("Toggle dark mode");

        String username = authContext.getAuthenticatedUser(DefaultOidcUser.class)
                .map(DefaultOidcUser::getName)
                .orElse("User");

        Avatar avatar = new Avatar(username);
        avatar.addClassNames(LumoUtility.Margin.Right.SMALL);

        MenuBar userMenu = new MenuBar();
        userMenu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        MenuItem userMenuItem = userMenu.addItem(avatar);
        SubMenu subMenu = userMenuItem.getSubMenu();
        subMenu.addItem("Settings", e -> UI.getCurrent().navigate("settings"));
        subMenu.addItem("Logout", e -> authContext.logout());

        HorizontalLayout header = new HorizontalLayout(toggle, title, themeToggle, userMenu);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(LumoUtility.Padding.Vertical.SMALL, LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav mainNav = new SideNav();
        mainNav.setLabel("MAIN NAVIGATION");
        mainNav.addItem(new SideNavItem("Dashboard", "", VaadinIcon.DASHBOARD.create()));
        mainNav.setWidthFull();

        SideNav warehouseNav = new SideNav();
        warehouseNav.setLabel("WAREHOUSE MANAGEMENT");
        SideNavItem inboundGroup = new SideNavItem("Inbound");
        inboundGroup.setPrefixComponent(VaadinIcon.DOWNLOAD_ALT.create());
        inboundGroup
                .addItem(new SideNavItem("Purchase Orders", "inbound/purchase-order", VaadinIcon.FILE_TEXT_O.create()));
        inboundGroup.addItem(new SideNavItem("Receiving", "inbound/receiving", VaadinIcon.INBOX.create()));
        warehouseNav.addItem(inboundGroup);
        warehouseNav.addItem(new SideNavItem("Inventory Stock", "inventory", VaadinIcon.PACKAGE.create()));
        warehouseNav.addItem(new SideNavItem("Outbound & Shipping", "outbound", VaadinIcon.UPLOAD_ALT.create()));
        warehouseNav.setWidthFull();

        SideNav prNav = new SideNav();
        prNav.setLabel("PURCHASE REQUISITION");
        prNav.addItem(new SideNavItem("Requisitions", "requisitions", VaadinIcon.CLIPBOARD_CHECK.create()));
        prNav.addItem(new SideNavItem("Request for Quotation", "rfq", VaadinIcon.MONEY.create()));
        prNav.addItem(new SideNavItem("Budget Planning", "budgets", VaadinIcon.CHART_LINE.create()));
        prNav.addItem(new SideNavItem("Transfer Requests", "transfers", VaadinIcon.EXCHANGE.create()));
        prNav.setWidthFull();

        SideNav masterNav = new SideNav();
        masterNav.setLabel("MASTER DATA");
        masterNav.addItem(new SideNavItem("Products", "master/products", VaadinIcon.RECORDS.create()));
        masterNav.addItem(new SideNavItem("Categories", "master/categories", VaadinIcon.TAGS.create()));
        masterNav.addItem(new SideNavItem("Suppliers", "master/suppliers", VaadinIcon.TRUCK.create()));
        masterNav.addItem(new SideNavItem("Customers", "master/customer", VaadinIcon.USERS.create()));
        masterNav.addItem(new SideNavItem("Warehouses", "master/warehouses", VaadinIcon.BUILDING.create()));
        masterNav.addItem(new SideNavItem("PR Items", "items", VaadinIcon.LIST.create()));
        masterNav.addItem(new SideNavItem("Companies", "company", VaadinIcon.OFFICE.create()));
        masterNav.setWidthFull();

        SideNav reportsNav = new SideNav();
        reportsNav.setLabel("REPORTS & ANALYTICS");
        reportsNav.addItem(new SideNavItem("WMS Reports", "reports", VaadinIcon.PIE_CHART.create()));
        reportsNav.addItem(new SideNavItem("PR Dashboard", "pr/dashboard", VaadinIcon.CHART.create()));
        reportsNav.setWidthFull();

        SideNav adminNav = new SideNav();
        adminNav.setLabel("ADMINISTRATION");
        adminNav.addItem(new SideNavItem("User Management", "users", VaadinIcon.USER_CHECK.create()));
        adminNav.addItem(new SideNavItem("System Settings", "settings", VaadinIcon.COG.create()));
        adminNav.addItem(new SideNavItem("Help & Support", "help", VaadinIcon.QUESTION_CIRCLE.create()));
        adminNav.setWidthFull();

        VerticalLayout navContainer = new VerticalLayout(mainNav, warehouseNav, prNav, masterNav, reportsNav, adminNav);
        navContainer.setPadding(true);
        navContainer.setSpacing(true);

        Scroller scroller = new Scroller(navContainer);
        scroller.addClassNames(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
    }
}
