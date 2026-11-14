package id.putra.core_erp.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Turns a request into a bucket key.
 *
 * <p>
 * Keys are read at the Vert.x filter stage, which runs before Quarkus resolves
 * the {@code SecurityIdentity}, so the authenticated user is not available here.
 * The OIDC session cookie is: it is issued after a successful Auth0 login and
 * carried by every subsequent request, which makes it the right identity proxy.
 *
 * <p>
 * The raw cookie is hashed before use. A bucket key ends up in heap dumps and,
 * if anyone ever logs it, in log files — a session token must not be recoverable
 * from either.
 */
public final class RateLimitKeyResolver {

	/**
	 * Session cookie set by the Quarkus OIDC {@code web-app} flow. Renaming it in
	 * {@code quarkus.oidc.authentication.cookie-name} breaks session keying, and
	 * every request falls back to its client address.
	 */
	private static final String SESSION_COOKIE = "q_session";

	private static final String SESSION_PREFIX = "session:";
	private static final String ADDRESS_PREFIX = "ip:";

	private RateLimitKeyResolver() {
	}

	/**
	 * @param sessionCookie raw {@code q_session} value, may be {@code null}
	 * @param remoteAddress peer address, may be {@code null}
	 * @return a stable key for the pair; never blank unless both inputs are blank
	 */
	public static String resolve(String sessionCookie, String remoteAddress) {
		if (sessionCookie != null && !sessionCookie.isBlank()) {
			return SESSION_PREFIX + sha256(sessionCookie);
		}
		if (remoteAddress != null && !remoteAddress.isBlank()) {
			return ADDRESS_PREFIX + remoteAddress;
		}
		// Both blank: every anonymous request from an unidentified peer shares one
		// bucket. Deliberately not a bypass — an unmetered fallback would be the
		// easier thing to reach for an attacker.
		return ADDRESS_PREFIX + "unknown";
	}

	private static String sha256(String value) {
		try {
			byte[] digest = MessageDigest.getInstance("SHA-256")
					.digest(value.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(digest);
		} catch (NoSuchAlgorithmException ex) {
			// SHA-256 is mandated by the JLS; unreachable on any conformant runtime.
			throw new IllegalStateException("SHA-256 unavailable", ex);
		}
	}
}
