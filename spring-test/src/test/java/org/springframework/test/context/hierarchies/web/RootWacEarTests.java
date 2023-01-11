package org.springframework.test.context.hierarchies.web;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 3.2.2
 */
@WebAppConfiguration
@ContextHierarchy(@ContextConfiguration)
class RootWacEarTests extends EarTests {

	@Configuration
	static class RootWacConfig {

		@Bean
		String root() {
			return "root";
		}
	}


	// -------------------------------------------------------------------------

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private String ear;

	@Autowired
	private String root;


	@Disabled("Superseded by verifyRootWacConfig()")
	@Test
	@Override
	void verifyEarConfig() {
		/* no-op */
	}

	@Test
	void verifyRootWacConfig() {
		ApplicationContext parent = wac.getParent();
		assertThat(parent).isNotNull();
		boolean condition = parent instanceof WebApplicationContext;
		assertThat(condition).isFalse();
		assertThat(ear).isEqualTo("ear");
		assertThat(root).isEqualTo("root");
	}

}
