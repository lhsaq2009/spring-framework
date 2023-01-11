package org.springframework.test.context.cache;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

/**
 * Integration test which verifies correct interaction between the
 * {@link DirtiesContextBeforeModesTestExecutionListener},
 * {@link DependencyInjectionTestExecutionListener}, and
 * {@link DirtiesContextTestExecutionListener} when
 * {@link DirtiesContext @DirtiesContext} is used at the method level.
 *
 * @author Sam Brannen
 * @since 4.2
 */
@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MethodLevelDirtiesContextTests {

	private static final AtomicInteger contextCount = new AtomicInteger();


	@Autowired
	private ConfigurableApplicationContext context;

	@Autowired
	private Integer count;


	@Test
	@Order(1)
	void basics() throws Exception {
		performAssertions(1);
	}

	@Test
	@Order(2)
	@DirtiesContext(methodMode = BEFORE_METHOD)
	void dirtyContextBeforeTestMethod() throws Exception {
		performAssertions(2);
	}

	@Test
	@Order(3)
	@DirtiesContext
	void dirtyContextAfterTestMethod() throws Exception {
		performAssertions(2);
	}

	@Test
	@Order(4)
	void backToBasics() throws Exception {
		performAssertions(3);
	}

	private void performAssertions(int expectedContextCreationCount) throws Exception {
		assertThat(this.context).as("context must not be null").isNotNull();
		assertThat(this.context.isActive()).as("context must be active").isTrue();

		assertThat(this.count).as("count must not be null").isNotNull();
		assertThat(this.count.intValue()).as("count: ").isEqualTo(expectedContextCreationCount);

		assertThat(contextCount.get()).as("context creation count: ").isEqualTo(expectedContextCreationCount);
	}


	@Configuration
	static class Config {

		@Bean
		Integer count() {
			return contextCount.incrementAndGet();
		}
	}

}
