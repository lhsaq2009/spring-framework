package org.springframework.test.context.hierarchies.standard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.HierarchyMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests that verify support for {@link DirtiesContext.HierarchyMode}
 * in conjunction with context hierarchies configured via {@link ContextHierarchy}.
 *
 * <p>Note that correct method execution order is essential, thus the use of
 * {@link TestMethodOrder @TestMethodOrder}.
 *
 * @author Sam Brannen
 * @since 3.2.2
 */
@ExtendWith(SpringExtension.class)
@ContextHierarchy({
	@ContextConfiguration(classes = DirtiesContextWithContextHierarchyTests.ParentConfig.class),
	@ContextConfiguration(classes = DirtiesContextWithContextHierarchyTests.ChildConfig.class)
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DirtiesContextWithContextHierarchyTests {

	@Autowired
	private StringBuffer foo;

	@Autowired
	private StringBuffer baz;

	@Autowired
	private ApplicationContext context;


	@BeforeEach
	void verifyContextHierarchy() {
		assertThat(context).as("child ApplicationContext").isNotNull();
		assertThat(context.getParent()).as("parent ApplicationContext").isNotNull();
		assertThat(context.getParent().getParent()).as("grandparent ApplicationContext").isNull();
	}

	@Test
	@Order(1)
	void verifyOriginalStateAndDirtyContexts() {
		assertOriginalState();
		reverseStringBuffers();
	}

	@Test
	@Order(2)
	@DirtiesContext
	void verifyContextsWereDirtiedAndTriggerExhaustiveCacheClearing() {
		assertDirtyParentContext();
		assertDirtyChildContext();
	}

	@Test
	@Order(3)
	@DirtiesContext(hierarchyMode = HierarchyMode.CURRENT_LEVEL)
	void verifyOriginalStateWasReinstatedAndDirtyContextsAndTriggerCurrentLevelCacheClearing() {
		assertOriginalState();
		reverseStringBuffers();
	}

	@Test
	@Order(4)
	void verifyParentContextIsStillDirtyButChildContextHasBeenReinstated() {
		assertDirtyParentContext();
		assertCleanChildContext();
	}

	private void reverseStringBuffers() {
		foo.reverse();
		baz.reverse();
	}

	private void assertOriginalState() {
		assertCleanParentContext();
		assertCleanChildContext();
	}

	private void assertCleanParentContext() {
		assertThat(foo.toString()).isEqualTo("foo");
	}

	private void assertCleanChildContext() {
		assertThat(baz.toString()).isEqualTo("baz-child");
	}

	private void assertDirtyParentContext() {
		assertThat(foo.toString()).isEqualTo("oof");
	}

	private void assertDirtyChildContext() {
		assertThat(baz.toString()).isEqualTo("dlihc-zab");
	}


	@Configuration
	static class ParentConfig {

		@Bean
		StringBuffer foo() {
			return new StringBuffer("foo");
		}

		@Bean
		StringBuffer baz() {
			return new StringBuffer("baz-parent");
		}
	}

	@Configuration
	static class ChildConfig {

		@Bean
		StringBuffer baz() {
			return new StringBuffer("baz-child");
		}
	}

}
