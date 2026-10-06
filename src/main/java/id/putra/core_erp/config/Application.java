package id.putra.core_erp.config;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@StyleSheet(Lumo.STYLESHEET)
@QuarkusMain
public class Application implements AppShellConfigurator {
	public static void main(String... args) {
		Quarkus.run(args);
	}
}
