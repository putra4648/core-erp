package id.putra.core_erp.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(NativeRuntimeHints.class)
public class NativeRuntimeHints implements RuntimeHintsRegistrar {

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
            MemberCategory.INVOKE_PUBLIC_METHODS
        );
    }
}
