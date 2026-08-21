package id.putra.core_erp.shared.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;

import jakarta.annotation.security.PermitAll;

@PermitAll
@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm;
    private final AuthenticationContext authContext;

    public LoginView(AuthenticationContext authContext) {
        this.authContext = authContext;
        addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.Flex.GROW);
        setSizeFull();

        // Centered layout
        VerticalLayout centeredLayout = new VerticalLayout();
        centeredLayout.addClassNames(LumoUtility.Padding.LARGE, LumoUtility.Margin.AUTO, LumoUtility.BoxSizing.BORDER);
        centeredLayout.setAlignItems(Alignment.CENTER);
        centeredLayout.setMaxWidth("400px");
        centeredLayout.setSpacing(true);

        // Header
        H1 header = new H1("CORE ERP");
        header.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.BOLD);
        header.getStyle().setMarginBottom("var(--lumo-space-m)");

        Paragraph subtitle = new Paragraph("Sign in to your account");

        // Login Form
        loginForm = new LoginForm();

        loginForm.setForgotPasswordButtonVisible(false);

        // Google Sign In Button
        Button auth0Button = new Button("Sign in with Auth0", click -> {
            // Redirect to Spring Security's OAuth2 provider
            getUI().ifPresent(ui -> {
                try {
                    ui.navigate("oauth2/authorization/auth0");
                } catch (Exception e) {
                    showError("Failed to start Auth0 sign in: " + e.getMessage());
                }
            });
        });
        auth0Button.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_PRIMARY);
        auth0Button.setWidthFull();
        auth0Button.getStyle().setMarginTop("var(--lumo-space-m)");
        auth0Button.getStyle().setMarginBottom("var(--lumo-space-m)");

        // Setup i18n
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("CORE ERP");
        i18n.getHeader().setDescription("Sign in to your account");

        i18n.setForm(new LoginI18n.Form());
        i18n.getForm().setUsername("Email");
        i18n.getForm().setPassword("Password");
        i18n.getForm().setSubmit("Sign in");

        loginForm.setI18n(i18n);

        centeredLayout.add(header, subtitle, loginForm, auth0Button);
        add(centeredLayout);
    }

    private void showError(String message) {
        loginForm.setError(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authContext.isAuthenticated()) {
            event.forwardTo(""); // Redirect to dashboard if already authenticated
        }
    }
}
