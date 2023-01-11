package org.springframework.context.testfixture.cache;

import org.junit.jupiter.api.Test;

import org.springframework.cache.support.AbstractValueAdaptingCache;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author Stephane Nicoll
 */
public abstract class AbstractValueAdaptingCacheTests<T extends AbstractValueAdaptingCache>
		extends AbstractCacheTests<T>  {

	protected final static String CACHE_NAME_NO_NULL = "testCacheNoNull";

	protected abstract T getCache(boolean allowNull);

	@Test
	public void testCachePutNullValueAllowNullFalse() {
		T cache = getCache(false);
		String key = createRandomKey();
		assertThatIllegalArgumentException().isThrownBy(() ->
				cache.put(key, null))
			.withMessageContaining(CACHE_NAME_NO_NULL)
			.withMessageContaining("is configured to not allow null values but null was provided");
	}

}
