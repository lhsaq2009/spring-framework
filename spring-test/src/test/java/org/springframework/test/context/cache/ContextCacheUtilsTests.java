package org.springframework.test.context.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.core.SpringProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.cache.ContextCache.DEFAULT_MAX_CONTEXT_CACHE_SIZE;
import static org.springframework.test.context.cache.ContextCache.MAX_CONTEXT_CACHE_SIZE_PROPERTY_NAME;
import static org.springframework.test.context.cache.ContextCacheUtils.retrieveMaxCacheSize;

/**
 * Unit tests for {@link ContextCacheUtils}.
 *
 * @author Sam Brannen
 * @since 4.3
 */
class ContextCacheUtilsTests {

	@BeforeEach
	@AfterEach
	void clearProperties() {
		System.clearProperty(MAX_CONTEXT_CACHE_SIZE_PROPERTY_NAME);
		SpringProperties.setProperty(MAX_CONTEXT_CACHE_SIZE_PROPERTY_NAME, null);
	}

	@Test
	void retrieveMaxCacheSizeFromDefault() {
		assertDefaultValue();
	}

	@Test
	void retrieveMaxCacheSizeFromBogusSystemProperty() {
		System.setProperty(MAX_CONTEXT_CACHE_SIZE_PROPERTY_NAME, "bogus");
		assertDefaultValue();
	}

	@Test
	void retrieveMaxCacheSizeFromBogusSpringProperty() {
		SpringProperties.setProperty(MAX_CONTEXT_CACHE_SIZE_PROPERTY_NAME, "bogus");
		assertDefaultValue();
	}

	@Test
	void retrieveMaxCacheSizeFromDecimalSpringProperty() {
		SpringProperties.setProperty(MAX_CONTEXT_CACHE_SIZE_PROPERTY_NAME, "3.14");
		assertDefaultValue();
	}

	@Test
	void retrieveMaxCacheSizeFromSystemProperty() {
		System.setProperty(MAX_CONTEXT_CACHE_SIZE_PROPERTY_NAME, "42");
		assertThat(retrieveMaxCacheSize()).isEqualTo(42);
	}

	@Test
	void retrieveMaxCacheSizeFromSystemPropertyContainingWhitespace() {
		System.setProperty(MAX_CONTEXT_CACHE_SIZE_PROPERTY_NAME, "42\t");
		assertThat(retrieveMaxCacheSize()).isEqualTo(42);
	}

	@Test
	void retrieveMaxCacheSizeFromSpringProperty() {
		SpringProperties.setProperty(MAX_CONTEXT_CACHE_SIZE_PROPERTY_NAME, "99");
		assertThat(retrieveMaxCacheSize()).isEqualTo(99);
	}

	private static void assertDefaultValue() {
		assertThat(retrieveMaxCacheSize()).isEqualTo(DEFAULT_MAX_CONTEXT_CACHE_SIZE);
	}

}
