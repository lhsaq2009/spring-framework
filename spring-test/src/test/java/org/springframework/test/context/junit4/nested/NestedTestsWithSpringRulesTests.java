package org.springframework.test.context.junit4.nested;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.nested.NestedTestsWithSpringRulesTests.TopLevelConfig;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JUnit 4 based integration tests for <em>nested</em> test classes that are
 * executed via a custom JUnit 4 {@link HierarchicalContextRunner} and Spring's
 * {@link SpringClassRule} and {@link SpringMethodRule} support.
 *
 * @author Sam Brannen
 * @since 5.0
 * @see org.springframework.test.context.junit.jupiter.nested.NestedTestsWithSpringAndJUnitJupiterTestCase
 */
@RunWith(HierarchicalContextRunner.class)
@ContextConfiguration(classes = TopLevelConfig.class)
public class NestedTestsWithSpringRulesTests extends SpringRuleConfigurer {

	@Autowired
	String foo;


	@Test
	public void topLevelTest() {
		assertThat(foo).isEqualTo("foo");
	}


	@ContextConfiguration(classes = NestedConfig.class)
	public class NestedTestCase extends SpringRuleConfigurer {

		@Autowired
		String bar;


		@Test
		public void nestedTest() throws Exception {
			// Note: the following would fail since TestExecutionListeners in
			// the Spring TestContext Framework are not applied to the enclosing
			// instance of an inner test class.
			//
			// assertEquals("foo", foo);

			assertThat(foo).as("@Autowired field in enclosing instance should be null.").isNull();
			assertThat(bar).isEqualTo("bar");
		}
	}

	// -------------------------------------------------------------------------

	@Configuration
	public static class TopLevelConfig {

		@Bean
		String foo() {
			return "foo";
		}
	}

	@Configuration
	public static class NestedConfig {

		@Bean
		String bar() {
			return "bar";
		}
	}

}
