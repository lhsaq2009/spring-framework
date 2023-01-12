package org.springframework.beans.factory.aspectj;

import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringConfiguredWithAutoProxyingTests {

	@Test
	@SuppressWarnings("resource")
	public void springConfiguredAndAutoProxyUsedTogether() {
		// instantiation is sufficient to trigger failure if this is going to fail...
		new ClassPathXmlApplicationContext("org/springframework/beans/factory/aspectj/springConfigured.xml");
	}

}
