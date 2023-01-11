package org.springframework.test.context.cache;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Collection of utility methods for testing scenarios involving the
 * {@link ContextCache}.
 *
 * @author Sam Brannen
 * @since 4.2
 */
public class ContextCacheTestUtils {

	/**
	 * Reset the state of the static context cache in {@link DefaultCacheAwareContextLoaderDelegate}.
	 */
	public static final void resetContextCache() {
		DefaultCacheAwareContextLoaderDelegate.defaultContextCache.reset();
	}

	/**
	 * Assert the statistics of the static context cache in {@link DefaultCacheAwareContextLoaderDelegate}.
	 *
	 * @param usageScenario the scenario in which the statistics are used
	 * @param expectedSize the expected number of contexts in the cache
	 * @param expectedHitCount the expected hit count
	 * @param expectedMissCount the expected miss count
	 */
	public static final void assertContextCacheStatistics(String usageScenario, int expectedSize, int expectedHitCount,
			int expectedMissCount) {
		assertContextCacheStatistics(DefaultCacheAwareContextLoaderDelegate.defaultContextCache, usageScenario,
			expectedSize, expectedHitCount, expectedMissCount);
	}

	/**
	 * Assert the statistics of the supplied context cache.
	 *
	 * @param contextCache the cache to assert against
	 * @param usageScenario the scenario in which the statistics are used
	 * @param expectedSize the expected number of contexts in the cache
	 * @param expectedHitCount the expected hit count
	 * @param expectedMissCount the expected miss count
	 */
	public static final void assertContextCacheStatistics(ContextCache contextCache, String usageScenario,
			int expectedSize, int expectedHitCount, int expectedMissCount) {

		assertThat(contextCache.size()).as("Verifying number of contexts in cache (" + usageScenario + ").").isEqualTo(expectedSize);
		assertThat(contextCache.getHitCount()).as("Verifying number of cache hits (" + usageScenario + ").").isEqualTo(expectedHitCount);
		assertThat(contextCache.getMissCount()).as("Verifying number of cache misses (" + usageScenario + ").").isEqualTo(expectedMissCount);
	}

}
