package org.springframework.aop.aspectj;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for overloaded advice.
 *
 * @author Adrian Colyer
 * @author Chris Beams
 */
public class OverloadedAdviceTests {

	@Test
	public void testExceptionOnConfigParsingWithMismatchedAdviceMethod() {
		try {
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		}
		catch (BeanCreationException ex) {
			Throwable cause = ex.getRootCause();
			boolean condition = cause instanceof IllegalArgumentException;
			assertThat(condition).as("Should be IllegalArgumentException").isTrue();
			assertThat(cause.getMessage().contains("invalidAbsoluteTypeName")).as("invalidAbsoluteTypeName should be detected by AJ").isTrue();
		}
	}

	@Test
	public void testExceptionOnConfigParsingWithAmbiguousAdviceMethod() {
		try {
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-ambiguous.xml", getClass());
		}
		catch (BeanCreationException ex) {
			Throwable cause = ex.getRootCause();
			boolean condition = cause instanceof IllegalArgumentException;
			assertThat(condition).as("Should be IllegalArgumentException").isTrue();
			assertThat(cause.getMessage().contains("Cannot resolve method 'myBeforeAdvice' to a unique method")).as("Cannot resolve method 'myBeforeAdvice' to a unique method").isTrue();
		}
	}

}


class OverloadedAdviceTestAspect {

	public void myBeforeAdvice(String name) {
		// no-op
	}

	public void myBeforeAdvice(int age) {
		// no-op
	}
}

