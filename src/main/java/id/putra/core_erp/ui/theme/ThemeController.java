package id.putra.core_erp.ui.theme;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;

import jakarta.servlet.http.Cookie;

/**
 * Reads, persists and applies the light/dark colour scheme preference.
 *
 * <p>
 * The preference is stored in an HTTP cookie, so it is already available while
 * the first request of a session is being rendered: the layout constructors
 * call {@link #applyStored()} before the initial HTML is written, which avoids
 * a flash of the wrong theme.
 *
 * <p>
 * Vaadin's own API is used to change the scheme
 * ({@link com.vaadin.flow.component.page.Page#setColorScheme(ColorScheme.Value)}).
 * The generic {@code ui.getElement().getThemeList().add("dark")} approach must
 * not be used: per the Vaadin 25 documentation it only matches Lumo's own
 * {@code [theme~="dark"]} selector, writes the attribute on {@code <body>} and
 * leaves {@code <html>} light.
 */
public final class ThemeController {

	/** Cookie name holding {@code light}, {@code dark} or {@code system}. */
	public static final String COOKIE_NAME = "core-erp-color-scheme";

	private static final String VALUE_LIGHT = "light";
	private static final String VALUE_DARK = "dark";
	private static final String VALUE_SYSTEM = "system";
	private static final int ONE_YEAR_IN_SECONDS = 60 * 60 * 24 * 365;

	private ThemeController() {
	}

	/**
	 * Applies the stored preference to the current UI. Safe to call from a
	 * layout constructor: does nothing when there is no cookie yet, leaving the
	 * app shell default ({@code @ColorScheme(LIGHT_DARK)}) in place.
	 */
	public static void applyStored() {
		UI ui = UI.getCurrent();
		if (ui == null) {
			return;
		}
		switch (storedValue()) {
		case VALUE_LIGHT -> ui.getPage().setColorScheme(ColorScheme.Value.LIGHT);
		case VALUE_DARK -> ui.getPage().setColorScheme(ColorScheme.Value.DARK);
		default -> {
			// no explicit choice — keep following the operating system preference
		}
		}
	}

	/**
	 * Switches the scheme and remembers the choice for one year.
	 *
	 * @param value
	 *            the scheme to apply
	 * @return the scheme that was applied
	 */
	public static ColorScheme.Value apply(ColorScheme.Value value) {
		UI ui = UI.getCurrent();
		if (ui == null) {
			return value;
		}
		ui.getPage().setColorScheme(value);
		writeCookie(value == ColorScheme.Value.DARK ? VALUE_DARK : VALUE_LIGHT);
		return value;
	}

	/**
	 * Resolves whether the browser is currently rendering the dark scheme and
	 * hands the answer to {@code callback} on the UI thread.
	 *
	 * <p>
	 * An explicit stored choice wins; otherwise the browser is asked, because
	 * the system preference is not visible to the server.
	 */
	public static void resolveDark(Consumer<Boolean> callback) {
		UI ui = UI.getCurrent();
		if (ui == null) {
			return;
		}
		switch (storedValue()) {
		case VALUE_LIGHT -> callback.accept(Boolean.FALSE);
		case VALUE_DARK -> callback.accept(Boolean.TRUE);
		default -> ui.getPage()
				.executeJs("return !!(window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches)")
				.then(Boolean.class, resolved -> callback.accept(Boolean.TRUE.equals(resolved)));
		}
	}

	private static String storedValue() {
		VaadinRequest request = VaadinService.getCurrentRequest();
		if (request == null || request.getCookies() == null) {
			return VALUE_SYSTEM;
		}
		return Arrays.stream(request.getCookies())
				.filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
				.map(Cookie::getValue)
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(VALUE_SYSTEM);
	}

	private static void writeCookie(String value) {
		VaadinResponse response = VaadinService.getCurrentResponse();
		if (response == null) {
			return;
		}
		Cookie cookie = new Cookie(COOKIE_NAME, value);
		cookie.setPath("/");
		cookie.setMaxAge(ONE_YEAR_IN_SECONDS);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}
}
