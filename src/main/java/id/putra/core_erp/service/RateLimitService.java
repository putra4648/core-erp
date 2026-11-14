package id.putra.core_erp.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import id.putra.core_erp.config.RateLimitProperties;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Token-bucket store keyed by session or client address.
 *
 * <p>
 * Buckets live in a bounded {@link ConcurrentHashMap} guarded by a size ceiling
 * and an idle timeout. Entries are swept lazily on write, so no scheduler thread
 * is needed; at the configured ceiling a sweep walks 10k cheap records, which is
 * well under the cost of the request that triggered it.
 *
 * <p>
 * Scope: per JVM. Two instances each keep their own buckets, so the effective
 * limit multiplies by the node count. Correct for the current single-node
 * deployment; a shared store is the upgrade path and only {@link #check}
 * changes.
 */
@ApplicationScoped
public class RateLimitService {

	/**
	 * Outcome of one {@link #check} call.
	 *
	 * @param allowed         whether the request may proceed
	 * @param remaining       tokens left in the bucket, {@code -1} when unlimited
	 * @param retryAfterSeconds seconds to wait; only meaningful when
	 *                        {@code !allowed}
	 */
	public record Decision(boolean allowed, long remaining, long retryAfterSeconds) {

		static final Decision UNLIMITED = new Decision(true, -1L, 0L);
	}

	private static final class Entry {
		final Bucket bucket;
		volatile Instant lastSeen;

		Entry(Bucket bucket, Instant lastSeen) {
			this.bucket = bucket;
			this.lastSeen = lastSeen;
		}
	}

	private final RateLimitProperties props;
	private final Map<String, Entry> buckets = new ConcurrentHashMap<>();

	@Inject
	public RateLimitService(RateLimitProperties props) {
		this.props = props;
	}

	/**
	 * Consumes one token for {@code key} and reports the verdict.
	 *
	 * <p>
	 * A single {@code tryConsumeAndReturnRemaining} call yields the decision, the
	 * remaining count and the refill wait together, so the filter never has to
	 * touch the bucket twice.
	 */
	public Decision check(String key) {
		if (!props.enabled() || key == null || key.isBlank()) {
			return Decision.UNLIMITED;
		}
		sweepIfNeeded();
		Entry entry = buckets.computeIfAbsent(key, k -> new Entry(newBucket(), Instant.now()));
		entry.lastSeen = Instant.now();

		ConsumptionProbe probe = entry.bucket.tryConsumeAndReturnRemaining(1);
		if (probe.isConsumed()) {
			return new Decision(true, probe.getRemainingTokens(), 0L);
		}
		// Ceil, so a sub-second wait never rounds down to an immediate retry that
		// would be rejected again.
		long seconds = Duration.ofNanos(probe.getNanosToWaitForRefill()).toSeconds();
		return new Decision(false, 0L, Math.max(1L, seconds + 1L));
	}

	/** Tracked key count. Test-facing. */
	int trackedKeys() {
		return buckets.size();
	}

	private Bucket newBucket() {
		return Bucket.builder().addLimit(limit -> limit
				.capacity(props.burstCapacity())
				.refillGreedy(props.requestsPerWindow(), props.window()))
				.build();
	}

	/**
	 * Evicts idle entries once the map reaches its ceiling, keeping the newest
	 * half. Lazy, so it costs nothing while traffic stays under the limit.
	 */
	private void sweepIfNeeded() {
		if (buckets.size() < props.maxBuckets()) {
			return;
		}
		Instant cutoff = Instant.now().minus(props.expireAfterAccess());
		buckets.entrySet().removeIf(e -> e.getValue().lastSeen.isBefore(cutoff));

		// Still full after the age sweep: the ceiling is genuinely reached, so drop
		// the oldest half rather than grow without bound.
		if (buckets.size() >= props.maxBuckets()) {
			buckets.entrySet().stream()
					.sorted((a, b) -> a.getValue().lastSeen.compareTo(b.getValue().lastSeen))
					.limit(buckets.size() / 2L)
					.map(Map.Entry::getKey)
					.toList()
					.forEach(buckets::remove);
		}
	}
}
