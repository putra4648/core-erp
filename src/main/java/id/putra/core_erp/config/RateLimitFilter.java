package id.putra.core_erp.config;

import java.util.List;
import java.util.Objects;

import org.jboss.logging.Logger;

import id.putra.core_erp.service.RateLimitKeyResolver;
import id.putra.core_erp.service.RateLimitService;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * Per-key HTTP rate limiting, installed as a Vert.x route on the main router.
 *
 * <p>
 * A Vert.x route rather than a Jakarta REST filter on purpose: this application
 * exposes no JAX-RS endpoints. Everything is served by the Vaadin servlet, the
 * {@code /VAADIN/*} static bundle and the {@code /connect/*} push channel, none
 * of which a REST filter would ever see.
 *
 * <p>
 * The route is registered by observing the {@link Router}, the documented Quarkus
 * extension point, so it runs for every request. No {@code SecurityIdentity} is
 * available this early in the chain, so keys come from
 * {@link RateLimitKeyResolver}.
 */
@ApplicationScoped
public class RateLimitFilter {

	private static final Logger LOG = Logger.getLogger(RateLimitFilter.class);

	/**
	 * Router order. Quarkus' own handlers sit at or below 10000 and custom routes
	 * are expected at 20000 or above; this sits in front of the application's own
	 * routes so the limit is applied before any application work starts.
	 */
	private static final int ROUTE_ORDER = 15_000;

	private static final String HEADER_REMAINING = "X-RateLimit-Remaining";
	private static final String HEADER_RETRY_AFTER = "Retry-After";

	/** Session cookie issued by the Quarkus OIDC {@code web-app} flow. */
	private static final String SESSION_COOKIE = "q_session";

	private final RateLimitService limiter;
	private final RateLimitProperties props;
	private final List<PathPattern> excluded;

	RateLimitFilter(RateLimitService limiter, RateLimitProperties props) {
		this.limiter = limiter;
		this.props = props;
		this.excluded = compileExclusions(props.excludedPaths());
	}

	void install(@Observes Router router) {
		router.route().order(ROUTE_ORDER).handler(this::handle);
		LOG.infof("Rate limiting enabled: %d requests per %s, burst %d, %d excluded pattern(s)",
				props.requestsPerWindow(), props.window(), props.burstCapacity(), excluded.size());
	}

	private void handle(RoutingContext rc) {
		if (!props.enabled() || isExcluded(rc.normalizedPath(), excluded)) {
			rc.next();
			return;
		}

		RateLimitService.Decision decision = limiter.check(RateLimitKeyResolver.resolve(
				sessionCookie(rc),
				rc.request().remoteAddress() == null ? null : rc.request().remoteAddress().host()));

		if (decision.allowed()) {
			if (decision.remaining() >= 0) {
				rc.response().putHeader(HEADER_REMAINING, Long.toString(decision.remaining()));
			}
			rc.next();
			return;
		}

		rc.response()
				.setStatusCode(429)
				.putHeader(HEADER_REMAINING, "0")
				.putHeader(HEADER_RETRY_AFTER, Long.toString(decision.retryAfterSeconds()))
				.putHeader("Content-Type", "text/plain")
				.end("Too many requests. Retry in " + decision.retryAfterSeconds() + "s.");
	}

	private static String sessionCookie(RoutingContext rc) {
		Cookie cookie = rc.request().getCookie(SESSION_COOKIE);
		return cookie == null ? null : cookie.getValue();
	}

	private static boolean isExcluded(String path, List<PathPattern> excluded) {
		for (PathPattern pattern : excluded) {
			if (pattern.matches(path)) {
				return true;
			}
		}
		return false;
	}

	/** Compiles the configured exclusions. Test-facing. */
	static List<PathPattern> compileExclusions(List<String> patterns) {
		return patterns.stream().map(PathPattern::compile).toList();
	}

	/** Test-facing: the matcher, without a request or a router. */
	static boolean isPathExcluded(String path, List<String> patterns) {
		return isExcluded(path, compileExclusions(patterns));
	}

	/**
	 * Ant-style path matcher supporting one trailing {@code /*} wildcard, all the
	 * configured patterns need. Avoids a dependency for four literals in
	 * {@code application.properties}.
	 */
	record PathPattern(String prefix, boolean wildcard) {

		static PathPattern compile(String pattern) {
			Objects.requireNonNull(pattern, "pattern");
			return pattern.endsWith("/*")
					? new PathPattern(pattern.substring(0, pattern.length() - 1), true)
					: new PathPattern(pattern, false);
		}

		boolean matches(String path) {
			return wildcard ? path.startsWith(prefix) : path.equals(prefix);
		}
	}
}
