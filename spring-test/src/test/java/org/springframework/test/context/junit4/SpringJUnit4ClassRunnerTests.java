package org.springframework.test.context.junit4;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;

import org.springframework.test.annotation.Timed;
import org.springframework.test.context.TestContextManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Unit tests for {@link SpringJUnit4ClassRunner}.
 *
 * @author Sam Brannen
 * @author Rick Evans
 * @since 2.5
 */
public class SpringJUnit4ClassRunnerTests {

	@Test
	public void checkThatExceptionsAreNotSilentlySwallowed() throws Exception {
		SpringJUnit4ClassRunner runner = new SpringJUnit4ClassRunner(getClass()) {

			@Override
			protected TestContextManager createTestContextManager(Class<?> clazz) {
				return new TestContextManager(clazz) {

					@Override
					public void prepareTestInstance(Object testInstance) {
						throw new RuntimeException(
							"This RuntimeException should be caught and wrapped in an Exception.");
					}
				};
			}
		};
		assertThatExceptionOfType(Exception.class).isThrownBy(
				runner::createTest);
	}

	@Test
	public void getSpringTimeoutViaMetaAnnotation() throws Exception {
		SpringJUnit4ClassRunner runner = new SpringJUnit4ClassRunner(getClass());
		long timeout = runner.getSpringTimeout(new FrameworkMethod(getClass().getDeclaredMethod(
			"springTimeoutWithMetaAnnotation")));
		assertThat(timeout).isEqualTo(10);
	}

	@Test
	public void getSpringTimeoutViaMetaAnnotationWithOverride() throws Exception {
		SpringJUnit4ClassRunner runner = new SpringJUnit4ClassRunner(getClass());
		long timeout = runner.getSpringTimeout(new FrameworkMethod(getClass().getDeclaredMethod(
			"springTimeoutWithMetaAnnotationAndOverride")));
		assertThat(timeout).isEqualTo(42);
	}

	// -------------------------------------------------------------------------

	@MetaTimed
	void springTimeoutWithMetaAnnotation() {
		/* no-op */
	}

	@MetaTimedWithOverride(millis = 42)
	void springTimeoutWithMetaAnnotationAndOverride() {
		/* no-op */
	}


	@Timed(millis = 10)
	@Retention(RetentionPolicy.RUNTIME)
	private static @interface MetaTimed {
	}

	@Timed(millis = 1000)
	@Retention(RetentionPolicy.RUNTIME)
	private static @interface MetaTimedWithOverride {

		long millis() default 1000;
	}

}
