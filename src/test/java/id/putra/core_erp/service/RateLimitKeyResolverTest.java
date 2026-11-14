package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RateLimitKeyResolverTest {

	@Test
	void sessionCookieWinsOverAddress() {
		String fromSession = RateLimitKeyResolver.resolve("cookie-value", "10.0.0.1");
		String fromAddress = RateLimitKeyResolver.resolve(null, "10.0.0.1");

		assertNotEquals(fromSession, fromAddress);
		assertTrue(fromSession.startsWith("session:"));
		assertTrue(fromAddress.startsWith("ip:"));
	}

	@Test
	void sameSessionYieldsSameKey() {
		assertEquals(
				RateLimitKeyResolver.resolve("cookie-value", "10.0.0.1"),
				RateLimitKeyResolver.resolve("cookie-value", "192.168.1.9"),
				"a signed-in user must not get a fresh bucket by changing networks");
	}

	@Test
	void differentSessionsDoNotCollide() {
		assertNotEquals(
				RateLimitKeyResolver.resolve("cookie-a", "10.0.0.1"),
				RateLimitKeyResolver.resolve("cookie-b", "10.0.0.1"));
	}

	@Test
	void rawCookieIsNeverPartOfTheKey() {
		// Bucket keys reach heap dumps and, if ever logged, log files.
		assertTrue(RateLimitKeyResolver.resolve("secret-cookie", null).indexOf("secret-cookie") < 0);
	}

	@Test
	void blankSessionFallsBackToAddress() {
		assertEquals(RateLimitKeyResolver.resolve(null, "10.0.0.1"),
				RateLimitKeyResolver.resolve("   ", "10.0.0.1"));
	}

	@Test
	void bothBlankSharesOneBucket() {
		assertEquals("ip:unknown", RateLimitKeyResolver.resolve(null, null));
		assertEquals("ip:unknown", RateLimitKeyResolver.resolve("", ""));
	}
}
