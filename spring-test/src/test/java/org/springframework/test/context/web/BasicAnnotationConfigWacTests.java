package org.springframework.test.context.web;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 3.2
 */
@ContextConfiguration
public class BasicAnnotationConfigWacTests extends AbstractBasicWacTests {

	@Configuration
	static class Config {

		@Bean
		public String foo() {
			return "enigma";
		}

		@Bean
		public ServletContextAwareBean servletContextAwareBean() {
			return new ServletContextAwareBean();
		}
	}

	@Autowired
	protected ServletContextAwareBean servletContextAwareBean;

	@Test
	public void fooEnigmaAutowired() {
		assertThat(foo).isEqualTo("enigma");
	}

	@Test
	public void servletContextAwareBeanProcessed() {
		assertThat(servletContextAwareBean).isNotNull();
		assertThat(servletContextAwareBean.servletContext).isNotNull();
	}

}
