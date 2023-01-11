package org.springframework.test.context.hierarchies.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sam Brannen
 * @since 3.2.2
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class EarTests {

	@Configuration
	static class EarConfig {

		@Bean
		String ear() {
			return "ear";
		}
	}


	// -------------------------------------------------------------------------

	@Autowired
	private ApplicationContext context;

	@Autowired
	private String ear;


	@Test
	void verifyEarConfig() {
		boolean condition = context instanceof WebApplicationContext;
		assertThat(condition).isFalse();
		assertThat(context.getParent()).isNull();
		assertThat(ear).isEqualTo("ear");
	}

}
