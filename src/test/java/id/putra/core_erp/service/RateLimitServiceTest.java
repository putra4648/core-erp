package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;

import id.putra.core_erp.config.RateLimitProperties;

/**
 * Plain JUnit: {@link RateLimitService} takes its configuration through the
 * constructor and holds no CDI state, so no container is needed.
 */
class RateLimitServiceTest {

	/** Fixed configuration — the small window keeps the refill assertion fast. */
	private static RateLimitProperties props(boolean enabled, int perWindow, Duration window, int burst) {
		return new RateLimitProperties() {
			@Override
			public boolean enabled() {
				return enabled;
			}

			@Override
			public int requestsPerWindow() {
				return perWindow;
			}

			@Override
			public Duration window() {
				return window;
			}

			@Override
			public int burstCapacity() {
				return burst;
			}

			@Override
			public int maxBuckets() {
				return 100;
			}

			@Override
			public Duration expireAfterAccess() {
				return Duration.ofMinutes(10);
			}

			@Override
			public List<String> excludedPaths() {
				return List.of();
			}
		};
	}

	@Test
	void rejectsOnceBucketIsExhausted() {
		RateLimitService service = new RateLimitService(props(true, 2, Duration.ofMinutes(1), 2));

		assertTrue(service.check("session:a").allowed());
		assertTrue(service.check("session:a").allowed());

		RateLimitService.Decision third = service.check("session:a");
		assertFalse(third.allowed(), "third request must be rejected");
		assertEquals(0L, third.remaining());
		assertTrue(third.retryAfterSeconds() >= 1L, "Retry-After must be a positive number of seconds");
	}

	@Test
	void keysAreIndependent() {
		RateLimitService service = new RateLimitService(props(true, 1, Duration.ofMinutes(1), 1));

		assertTrue(service.check("session:a").allowed());
		assertFalse(service.check("session:a").allowed());

		assertTrue(service.check("session:b").allowed(), "a exhausted key must not affect another");
	}

	@Test
	void reportsDecreasingRemainingTokens() {
		RateLimitService service = new RateLimitService(props(true, 10, Duration.ofMinutes(1), 3));

		assertEquals(2L, service.check("session:a").remaining());
		assertEquals(1L, service.check("session:a").remaining());
		assertEquals(0L, service.check("session:a").remaining());
	}

	@Test
	void disabledMeansAlwaysAllowed() {
		RateLimitService service = new RateLimitService(props(false, 1, Duration.ofMinutes(1), 1));

		for (int i = 0; i < 50; i++) {
			assertTrue(service.check("session:a").allowed());
		}
		assertEquals(0, service.trackedKeys(), "no buckets are created while disabled");
	}

	@Test
	void blankKeyIsNotBucketed() {
		RateLimitService service = new RateLimitService(props(true, 1, Duration.ofMinutes(1), 1));

		assertTrue(service.check(null).allowed());
		assertTrue(service.check("  ").allowed());
		assertEquals(0, service.trackedKeys());
	}

	@Test
	void refillsAfterTheWindow() throws InterruptedException {
		RateLimitService service = new RateLimitService(props(true, 1, Duration.ofMillis(200), 1));

		assertTrue(service.check("session:a").allowed());
		assertFalse(service.check("session:a").allowed());

		Thread.sleep(400);

		assertTrue(service.check("session:a").allowed(), "token must be available again past the window");
	}
}
