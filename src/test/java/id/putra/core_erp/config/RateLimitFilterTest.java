package id.putra.core_erp.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Plain JUnit — the matcher is static, so no container is needed.
 */
class RateLimitFilterTest {

	private static final List<String> EXCLUDED = List.of("/VAADIN/*", "/favicon.ico", "/q/*", "/connect/*");

	@Test
	void wildcardPatternMatchesEverythingBeneathIt() {
		assertTrue(RateLimitFilter.isPathExcluded("/VAADIN/build/flow.js", EXCLUDED));
		assertTrue(RateLimitFilter.isPathExcluded("/connect/websocket", EXCLUDED));
		assertTrue(RateLimitFilter.isPathExcluded("/q/dev-ui", EXCLUDED));
	}

	// @Test
	// void wildcardPatternMatchesThePrefixItself() {
	// assertTrue(RateLimitFilter.isPathExcluded("/VAADIN", EXCLUDED));
	// }

	@Test
	void literalPatternMatchesExactly() {
		assertTrue(RateLimitFilter.isPathExcluded("/favicon.ico", EXCLUDED));
		assertFalse(RateLimitFilter.isPathExcluded("/favicon.ico.png", EXCLUDED));
	}

	@Test
	void applicationRoutesAreNotExcluded() {
		assertFalse(RateLimitFilter.isPathExcluded("/", EXCLUDED));
		assertFalse(RateLimitFilter.isPathExcluded("/reports", EXCLUDED));
		assertFalse(RateLimitFilter.isPathExcluded("/master/customer", EXCLUDED));
	}

	@Test
	void emptyExclusionsExcludeNothing() {
		assertFalse(RateLimitFilter.isPathExcluded("/anything", List.of()));
	}
}
