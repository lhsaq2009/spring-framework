package org.springframework.context.annotation;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.testfixture.beans.TestBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TCK-style unit tests for handling circular use of the {@link Import} annotation.
 * Explore the subclass hierarchy for specific concrete implementations.
 *
 * @author Chris Beams
 */
public abstract class AbstractCircularImportDetectionTests {

	protected abstract ConfigurationClassParser newParser();

	protected abstract String loadAsConfigurationSource(Class<?> clazz) throws Exception;


	@Test
	public void simpleCircularImportIsDetected() throws Exception {
		boolean threw = false;
		try {
			newParser().parse(loadAsConfigurationSource(A.class), "A");
		}
		catch (BeanDefinitionParsingException ex) {
			assertThat(ex.getMessage().contains(
							"Illegal attempt by @Configuration class 'AbstractCircularImportDetectionTests.B' " +
							"to import class 'AbstractCircularImportDetectionTests.A'")).as("Wrong message. Got: " + ex.getMessage()).isTrue();
			threw = true;
		}
		assertThat(threw).isTrue();
	}

	@Test
	public void complexCircularImportIsDetected() throws Exception {
		boolean threw = false;
		try {
			newParser().parse(loadAsConfigurationSource(X.class), "X");
		}
		catch (BeanDefinitionParsingException ex) {
			assertThat(ex.getMessage().contains(
							"Illegal attempt by @Configuration class 'AbstractCircularImportDetectionTests.Z2' " +
							"to import class 'AbstractCircularImportDetectionTests.Z'")).as("Wrong message. Got: " + ex.getMessage()).isTrue();
			threw = true;
		}
		assertThat(threw).isTrue();
	}


	@Configuration
	@Import(B.class)
	static class A {

		@Bean
		TestBean b1() {
			return new TestBean();
		}
	}


	@Configuration
	@Import(A.class)
	static class B {

		@Bean
		TestBean b2() {
			return new TestBean();
		}
	}


	@Configuration
	@Import({Y.class, Z.class})
	class X {

		@Bean
		TestBean x() {
			return new TestBean();
		}
	}


	@Configuration
	class Y {

		@Bean
		TestBean y() {
			return new TestBean();
		}
	}


	@Configuration
	@Import({Z1.class, Z2.class})
	class Z {

		@Bean
		TestBean z() {
			return new TestBean();
		}
	}


	@Configuration
	class Z1 {

		@Bean
		TestBean z1() {
			return new TestBean();
		}
	}


	@Configuration
	@Import(Z.class)
	class Z2 {

		@Bean
		TestBean z2() {
			return new TestBean();
		}
	}

}
