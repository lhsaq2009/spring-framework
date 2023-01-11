package org.springframework.web.util;

import org.junit.jupiter.api.Test;

import org.springframework.web.testfixture.servlet.MockServletContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Marten Deinum
 * @since 3.2.2
 */
public class ServletContextPropertyUtilsTests {

	@Test
	public void resolveAsServletContextInitParameter() {
		MockServletContext servletContext = new MockServletContext();
		servletContext.setInitParameter("test.prop", "bar");
		String resolved = ServletContextPropertyUtils.resolvePlaceholders("${test.prop:foo}", servletContext);
		assertThat(resolved).isEqualTo("bar");
	}

	@Test
	public void fallbackToSystemProperties() {
		MockServletContext servletContext = new MockServletContext();
		System.setProperty("test.prop", "bar");
		try {
			String resolved = ServletContextPropertyUtils.resolvePlaceholders("${test.prop:foo}", servletContext);
			assertThat(resolved).isEqualTo("bar");
		}
		finally {
			System.clearProperty("test.prop");
		}
	}
}
