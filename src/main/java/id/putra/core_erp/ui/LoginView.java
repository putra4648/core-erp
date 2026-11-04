package id.putra.core_erp.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.ui.theme.ThemeController;
import id.putra.core_erp.ui.theme.ThemeToggleButton;

@AnonymousAllowed
@Route("login")
@PageTitle("Login | CORE ERP")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	// Quarkus OIDC (web-app) starts the authorization-code flow automatically when an
	// unauthenticated request reaches a protected path. Redirecting to the protected
	// root "/" therefore triggers the redirect to Auth0. Quarkus does NOT expose the
	// Spring Security "/oauth2/authorization/{registrationId}" endpoint.
	public static final String AUTH0_URL = "/";

	public LoginView() {
		ThemeController.applyStored();

		addClassNames("app-login-shell", LumoUtility.Flex.GROW);
		setSizeFull();
		setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		setAlignItems(FlexComponent.Alignment.CENTER);

		// Colour scheme switch, pinned to the top-right corner of the view
		ThemeToggleButton themeToggle = new ThemeToggleButton();
		themeToggle.addClassName("app-login-toggle");

		// Centered Card
		VerticalLayout card = new VerticalLayout();
		card.addClassNames("app-card", LumoUtility.Padding.XLARGE, LumoUtility.BoxSizing.BORDER);
		card.setAlignItems(FlexComponent.Alignment.CENTER);
		card.setMaxWidth("420px");
		card.setWidth("90%");
		card.setSpacing(true);

		// Header
		H1 header = new H1("CORE ERP");
		header.addClassNames(LumoUtility.FontSize.XXLARGE, LumoUtility.FontWeight.EXTRABOLD,
				LumoUtility.TextColor.PRIMARY, LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.XSMALL);

		Paragraph subtitle = new Paragraph("Enterprise Resource Planning");
		subtitle.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY, LumoUtility.Margin.Top.NONE,
				LumoUtility.Margin.Bottom.LARGE);

		Span infoBadge = new Span("Single Sign-On Authentication");
		infoBadge.getElement().getThemeList().add("badge pill");
		infoBadge.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

		// Sign In Button
		Button auth0Button = new Button("Sign in with Auth0", VaadinIcon.SIGN_IN.create(), click -> {
			UI.getCurrent().getPage().setLocation(AUTH0_URL);
		});
		auth0Button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
		auth0Button.setWidthFull();
		auth0Button.getStyle().setCursor("pointer");

		Paragraph helperText = new Paragraph("Secure enterprise identity powered by Auth0");
		helperText.addClassNames(LumoUtility.FontSize.XSMALL, LumoUtility.TextColor.TERTIARY,
				LumoUtility.Margin.Top.MEDIUM, LumoUtility.Margin.Bottom.NONE);

		card.add(header, subtitle, infoBadge, auth0Button, helperText);
		add(card, themeToggle);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// if (authContext.isAuthenticated()) {
		// event.forwardTo("");
		// return;
		// }

		if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
			Notification notification = Notification.show(
					"Authentication failed. Please check your credentials or contact administrator.", 5000,
					Notification.Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}
}
