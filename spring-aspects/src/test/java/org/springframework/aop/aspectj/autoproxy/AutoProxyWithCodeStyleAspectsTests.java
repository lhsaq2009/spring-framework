package org.springframework.aop.aspectj.autoproxy;

import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Adrian Colyer
 */
public class AutoProxyWithCodeStyleAspectsTests {

	@Test
	@SuppressWarnings("resource")
	public void noAutoproxyingOfAjcCompiledAspects() {
		new ClassPathXmlApplicationContext("org/springframework/aop/aspectj/autoproxy/ajcAutoproxyTests.xml");
	}

}
