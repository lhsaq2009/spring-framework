package org.springframework.core.env;

import org.junit.jupiter.api.Test;

import org.springframework.context.support.GenericApplicationContext;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

class PropertyPlaceholderConfigurerEnvironmentIntegrationTests {

	@Test
	@SuppressWarnings("deprecation")
	void test() {
		GenericApplicationContext ctx = new GenericApplicationContext();
		ctx.registerBeanDefinition("ppc",
				rootBeanDefinition(org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.class)
				.addPropertyValue("searchSystemEnvironment", false)
				.getBeanDefinition());
		ctx.refresh();
		ctx.getBean("ppc");
		ctx.close();
	}

}
