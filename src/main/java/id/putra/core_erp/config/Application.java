package id.putra.core_erp.config;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.theme.lumo.Lumo;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/*
 * Lumo base styles + Lumo utility classes (required for every LumoUtility.*
 * class name used by the views) + the CORE ERP brand palette.
 * styles.css must stay last so its token overrides win.
 */
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
@StyleSheet("styles.css")
@ColorScheme(ColorScheme.Value.LIGHT_DARK)
@QuarkusMain
public class Application implements AppShellConfigurator {
	public static void main(String... args) {
		Quarkus.run(args);
	}
}
