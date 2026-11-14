package id.putra.core_erp.config;

import java.time.Duration;
import java.util.List;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * Rate-limit settings, bound from {@code erp.rate-limit.*}.
 *
 * <p>
 * Buckets are held per application instance. That is correct for the current
 * single-node deployment; see {@code README.md} for the multi-node caveat.
 */
@ConfigMapping(prefix = "erp.rate-limit")
public interface RateLimitProperties {

	/** Master switch. Turned off in tests so view fixtures never trip the limiter. */
	@WithDefault("true")
	boolean enabled();

	/** Sustained requests allowed per key per {@link #window()}. */
	@WithDefault("120")
	int requestsPerWindow();

	/** Window over which {@link #requestsPerWindow()} refills. */
	@WithDefault("1M")
	Duration window();

	/**
	 * Tokens available to a key that has not been seen in a while — the page-load
	 * burst. Must not exceed {@link #requestsPerWindow()} by much, or the first
	 * request after the window is effectively unmetered.
	 */
	@WithDefault("60")
	int burstCapacity();

	/** Ceiling on tracked keys. Bounds heap; not a security limit. */
	@WithDefault("10000")
	int maxBuckets();

	/** Idle keys older than this are evicted on the next sweep. */
	@WithDefault("10M")
	Duration expireAfterAccess();

	/**
	 * Ant-style path patterns exempt from limiting. Static bundles and the Vaadin
	 * push channel must stay here, otherwise the UI breaks under normal use.
	 */
	@WithDefault("/VAADIN/*,/favicon.ico,/q/*,/connect/*")
	List<String> excludedPaths();
}
