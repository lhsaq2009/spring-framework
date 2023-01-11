package org.springframework.cache.ehcache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.testfixture.cache.AbstractCacheTests;
import org.springframework.core.testfixture.EnabledForTestGroups;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.core.testfixture.TestGroup.LONG_RUNNING;

/**
 * @author Costin Leau
 * @author Stephane Nicoll
 * @author Juergen Hoeller
 */
public class EhCacheCacheTests extends AbstractCacheTests<EhCacheCache> {

	private CacheManager cacheManager;

	private Ehcache nativeCache;

	private EhCacheCache cache;


	@BeforeEach
	public void setup() {
		cacheManager = new CacheManager(new Configuration().name("EhCacheCacheTests")
				.defaultCache(new CacheConfiguration("default", 100)));
		nativeCache = new net.sf.ehcache.Cache(new CacheConfiguration(CACHE_NAME, 100));
		cacheManager.addCache(nativeCache);

		cache = new EhCacheCache(nativeCache);
	}

	@AfterEach
	public void shutdown() {
		cacheManager.shutdown();
	}


	@Override
	protected EhCacheCache getCache() {
		return cache;
	}

	@Override
	protected Ehcache getNativeCache() {
		return nativeCache;
	}


	@Test
	@EnabledForTestGroups(LONG_RUNNING)
	public void testExpiredElements() throws Exception {
		String key = "brancusi";
		String value = "constantin";
		Element brancusi = new Element(key, value);
		// ttl = 10s
		brancusi.setTimeToLive(3);
		nativeCache.put(brancusi);

		assertThat(cache.get(key).get()).isEqualTo(value);
		// wait for the entry to expire
		Thread.sleep(5 * 1000);
		assertThat(cache.get(key)).isNull();
	}

}
