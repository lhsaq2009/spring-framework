package org.springframework.beans.factory.xml;

import org.junit.jupiter.api.Test;

import org.springframework.beans.testfixture.beans.ITestBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for combining the expression language and the p namespace. Due to the required EL dependency, this test is in
 * context module rather than the beans module.
 *
 * @author Arjen Poutsma
 */
public class SimplePropertyNamespaceHandlerWithExpressionLanguageTests {

	@Test
	public void combineWithExpressionLanguage() {
		ApplicationContext applicationContext =
				new ClassPathXmlApplicationContext("simplePropertyNamespaceHandlerWithExpressionLanguageTests.xml",
						getClass());
		ITestBean foo = applicationContext.getBean("foo", ITestBean.class);
		ITestBean bar = applicationContext.getBean("bar", ITestBean.class);
		assertThat(foo.getName()).as("Invalid name").isEqualTo("Baz");
		assertThat(bar.getName()).as("Invalid name").isEqualTo("Baz");
	}

}
