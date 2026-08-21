package id.putra.core_erp;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class CoreErpApplicationTests {

	@Test
	void createApplicationModuleModel() {
		ApplicationModules modules = ApplicationModules.of(CoreErpApplication.class);
		modules.forEach(System.out::println);
		modules.verify();
		new Documenter(modules).writeDocumentation();
	}
}

