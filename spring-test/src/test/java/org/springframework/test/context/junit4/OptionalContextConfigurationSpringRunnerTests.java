package org.springframework.test.context.junit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JUnit 4 based integration test which verifies that
 * {@link ContextConfiguration @ContextConfiguration} is optional.
 *
 * @author Phillip Webb
 * @author Sam Brannen
 * @since 4.3
 */
@RunWith(SpringRunner.class)
public class OptionalContextConfigurationSpringRunnerTests {

	@Autowired
	String foo;


	@Test
	public void contextConfigurationAnnotationIsOptional() {
		assertThat(foo).isEqualTo("foo");
	}


	@Configuration
	static class Config {

		@Bean
		String foo() {
			return "foo";
		}
	}

}
