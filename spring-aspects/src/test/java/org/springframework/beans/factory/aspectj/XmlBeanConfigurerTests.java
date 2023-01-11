package org.springframework.beans.factory.aspectj;

import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Chris Beams
 */
public class XmlBeanConfigurerTests {

	@Test
	public void injection() {
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/beans/factory/aspectj/beanConfigurerTests.xml")) {

			ShouldBeConfiguredBySpring myObject = new ShouldBeConfiguredBySpring();
			assertThat(myObject.getName()).isEqualTo("Rod");
		}
	}

}
