package org.springframework.cache.jcache;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.testfixture.cache.AbstractCacheAnnotationTests;
import org.springframework.context.testfixture.cache.SomeCustomKeyGenerator;
import org.springframework.context.testfixture.cache.beans.AnnotatedClassCacheableService;
import org.springframework.context.testfixture.cache.beans.CacheableService;
import org.springframework.context.testfixture.cache.beans.DefaultCacheableService;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.testfixture.CallCountingTransactionManager;

/**
 * @author Stephane Nicoll
 * @author Juergen Hoeller
 */
public class JCacheEhCacheAnnotationTests extends AbstractCacheAnnotationTests {

	private final TransactionTemplate txTemplate = new TransactionTemplate(new CallCountingTransactionManager());

	private CacheManager jCacheManager;


	@Override
	protected ConfigurableApplicationContext getApplicationContext() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getBeanFactory().registerSingleton("cachingProvider", getCachingProvider());
		context.register(EnableCachingConfig.class);
		context.refresh();
		jCacheManager = context.getBean("jCacheManager", CacheManager.class);
		return context;
	}

	protected CachingProvider getCachingProvider() {
		return Caching.getCachingProvider("org.ehcache.jcache.JCacheCachingProvider");
	}

	@AfterEach
	public void shutdown() {
		if (jCacheManager != null) {
			jCacheManager.close();
		}
	}


	@Override
	@Test
	@Disabled("Multi cache manager support to be added")
	public void testCustomCacheManager() {
	}

	@Test
	public void testEvictWithTransaction() {
		txTemplate.executeWithoutResult(s -> testEvict(this.cs, false));
	}

	@Test
	public void testEvictEarlyWithTransaction() {
		txTemplate.executeWithoutResult(s -> testEvictEarly(this.cs));
	}

	@Test
	public void testEvictAllWithTransaction() {
		txTemplate.executeWithoutResult(s -> testEvictAll(this.cs, false));
	}

	@Test
	public void testEvictAllEarlyWithTransaction() {
		txTemplate.executeWithoutResult(s -> testEvictAllEarly(this.cs));
	}


	@Configuration
	@EnableCaching
	static class EnableCachingConfig extends CachingConfigurerSupport {

		@Autowired
		CachingProvider cachingProvider;

		@Override
		@Bean
		public org.springframework.cache.CacheManager cacheManager() {
			JCacheCacheManager cm = new JCacheCacheManager(jCacheManager());
			cm.setTransactionAware(true);
			return cm;
		}

		@Bean
		public CacheManager jCacheManager() {
			CacheManager cacheManager = this.cachingProvider.getCacheManager();
			MutableConfiguration<Object, Object> mutableConfiguration = new MutableConfiguration<>();
			mutableConfiguration.setStoreByValue(false);  // otherwise value has to be Serializable
			cacheManager.createCache("testCache", mutableConfiguration);
			cacheManager.createCache("primary", mutableConfiguration);
			cacheManager.createCache("secondary", mutableConfiguration);
			return cacheManager;
		}

		@Bean
		public CacheableService<?> service() {
			return new DefaultCacheableService();
		}

		@Bean
		public CacheableService<?> classService() {
			return new AnnotatedClassCacheableService();
		}

		@Override
		@Bean
		public KeyGenerator keyGenerator() {
			return new SimpleKeyGenerator();
		}

		@Bean
		public KeyGenerator customKeyGenerator() {
			return new SomeCustomKeyGenerator();
		}
	}

}
