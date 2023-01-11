package org.springframework.cache.ehcache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManagerTests;

/**
 * @author Stephane Nicoll
 */
public class EhCacheCacheManagerTests extends AbstractTransactionSupportingCacheManagerTests<EhCacheCacheManager> {

	private CacheManager nativeCacheManager;

	private EhCacheCacheManager cacheManager;

	private EhCacheCacheManager transactionalCacheManager;


	@BeforeEach
	public void setup() {
		nativeCacheManager = new CacheManager(new Configuration().name("EhCacheCacheManagerTests")
				.defaultCache(new CacheConfiguration("default", 100)));
		addNativeCache(CACHE_NAME);

		cacheManager = new EhCacheCacheManager(nativeCacheManager);
		cacheManager.setTransactionAware(false);
		cacheManager.afterPropertiesSet();

		transactionalCacheManager = new EhCacheCacheManager(nativeCacheManager);
		transactionalCacheManager.setTransactionAware(true);
		transactionalCacheManager.afterPropertiesSet();
	}

	@AfterEach
	public void shutdown() {
		nativeCacheManager.shutdown();
	}


	@Override
	protected EhCacheCacheManager getCacheManager(boolean transactionAware) {
		if (transactionAware) {
			return transactionalCacheManager;
		}
		else {
			return cacheManager;
		}
	}

	@Override
	protected Class<? extends org.springframework.cache.Cache> getCacheType() {
		return EhCacheCache.class;
	}

	@Override
	protected void addNativeCache(String cacheName) {
		nativeCacheManager.addCache(cacheName);
	}

	@Override
	protected void removeNativeCache(String cacheName) {
		nativeCacheManager.removeCache(cacheName);
	}

}
