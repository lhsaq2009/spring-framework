package org.springframework.web.servlet.view.groovy;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import org.springframework.beans.DirectFieldAccessor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for
 * {@link org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver}.
 *
 * @author Brian Clozel
 */
public class GroovyMarkupViewResolverTests {

	@Test
	public void viewClass() throws Exception {
		GroovyMarkupViewResolver resolver = new GroovyMarkupViewResolver();
		assertThat(resolver.requiredViewClass()).isEqualTo(GroovyMarkupView.class);
		DirectFieldAccessor viewAccessor = new DirectFieldAccessor(resolver);
		Class<?> viewClass = (Class<?>) viewAccessor.getPropertyValue("viewClass");
		assertThat(viewClass).isEqualTo(GroovyMarkupView.class);
	}

	@Test
	public void cacheKey() throws Exception {
		GroovyMarkupViewResolver resolver = new GroovyMarkupViewResolver();
		String cacheKey = (String) resolver.getCacheKey("test", Locale.US);
		assertThat(cacheKey).isNotNull();
		assertThat(cacheKey).isEqualTo("test_en_US");
	}

}
