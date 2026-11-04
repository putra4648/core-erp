package id.putra.core_erp.ui.theme;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.ColorScheme;

/**
 * Header button that switches the application between the light and dark
 * colour schemes.
 *
 * <p>
 * The icon shows the scheme a click will apply: a moon while the app is light,
 * a sun while it is dark.
 */
public class ThemeToggleButton extends Button {

	private static final long serialVersionUID = 1L;

	/** Whether the dark scheme is currently rendered. */
	private boolean dark;

	public ThemeToggleButton() {
		addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		setAriaLabel("Toggle dark mode");
		getElement().setAttribute("title", "Toggle light / dark theme");

		addClickListener(event -> {
			ThemeController.apply(dark ? ColorScheme.Value.LIGHT : ColorScheme.Value.DARK);
			dark = !dark;
			refreshIcon();
		});

		refreshIcon();
		ThemeController.resolveDark(this::setDark);
	}

	private void setDark(boolean dark) {
		this.dark = dark;
		refreshIcon();
	}

	private void refreshIcon() {
		setIcon(dark ? VaadinIcon.SUN_O.create() : VaadinIcon.MOON.create());
	}
}
