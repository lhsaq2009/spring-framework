package org.springframework.cache.config;

import org.junit.jupiter.api.Test;

import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.testfixture.cache.AbstractCacheAnnotationTests;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Costin Leau
 * @author Chris Beams
 * @author Stephane Nicoll
 */
public class AnnotationNamespaceDrivenTests extends AbstractCacheAnnotationTests {

	@Override
	protected ConfigurableApplicationContext getApplicationContext() {
		return new GenericXmlApplicationContext(
				"/org/springframework/cache/config/annotationDrivenCacheNamespace.xml");
	}

	@Test
	public void testKeyStrategy() {
		CacheInterceptor ci = this.ctx.getBean(
				"org.springframework.cache.interceptor.CacheInterceptor#0", CacheInterceptor.class);
		assertThat(ci.getKeyGenerator()).isSameAs(this.ctx.getBean("keyGenerator"));
	}

	@Test
	public void cacheResolver() {
		ConfigurableApplicationContext context = new GenericXmlApplicationContext(
				"/org/springframework/cache/config/annotationDrivenCacheNamespace-resolver.xml");

		CacheInterceptor ci = context.getBean(CacheInterceptor.class);
		assertThat(ci.getCacheResolver()).isSameAs(context.getBean("cacheResolver"));
		context.close();
	}

	@Test
	public void bothSetOnlyResolverIsUsed() {
		ConfigurableApplicationContext context = new GenericXmlApplicationContext(
				"/org/springframework/cache/config/annotationDrivenCacheNamespace-manager-resolver.xml");

		CacheInterceptor ci = context.getBean(CacheInterceptor.class);
		assertThat(ci.getCacheResolver()).isSameAs(context.getBean("cacheResolver"));
		context.close();
	}

	@Test
	public void testCacheErrorHandler() {
		CacheInterceptor ci = this.ctx.getBean(
				"org.springframework.cache.interceptor.CacheInterceptor#0", CacheInterceptor.class);
		assertThat(ci.getErrorHandler()).isSameAs(this.ctx.getBean("errorHandler", CacheErrorHandler.class));
	}

}
