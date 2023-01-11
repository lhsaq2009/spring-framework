package org.springframework.cache.config;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * AOP advice specific parsing tests.
 *
 * @author Stephane Nicoll
 */
public class CacheAdviceParserTests {

	@Test
	public void keyAndKeyGeneratorCannotBeSetTogether() {
		assertThatExceptionOfType(BeanDefinitionStoreException.class).isThrownBy(() ->
				new GenericXmlApplicationContext("/org/springframework/cache/config/cache-advice-invalid.xml"));
		// TODO better exception handling
	}

}
