package id.putra.core_erp;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;

@StyleSheet(Lumo.STYLESHEET)
@SpringBootApplication
@ImportRuntimeHints(CoreErpApplication.Hints.class)
public class CoreErpApplication implements AppShellConfigurator {

	public static class Hints implements RuntimeHintsRegistrar {
		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			// Register Vaadin and web application resource patterns
			hints.resources().registerPattern("META-INF/VAADIN/*");
			hints.resources().registerPattern("META-INF/VAADIN/webapp/*");
			hints.resources().registerPattern("META-INF/VAADIN/config/*");
			hints.resources().registerPattern("vaadin-i18n/*");
			hints.resources().registerPattern("static/*");
			hints.resources().registerPattern("templates/*");
			hints.resources().registerPattern("application*.properties");
			hints.resources().registerPattern("application*.yml");
			hints.resources().registerPattern("application*.yaml");

			// Reflection hints for member categories on POI/ZXing if needed
			hints.reflection().registerType(
					com.google.zxing.qrcode.QRCodeWriter.class,
					MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
					MemberCategory.INVOKE_PUBLIC_METHODS);

			hints.reflection().registerType(java.util.UUID[].class,
					MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
					MemberCategory.INVOKE_PUBLIC_METHODS);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(CoreErpApplication.class, args);
	}

}
