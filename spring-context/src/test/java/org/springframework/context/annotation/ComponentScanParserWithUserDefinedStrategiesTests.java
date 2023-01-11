package org.springframework.context.annotation;

import org.junit.jupiter.api.Test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Mark Fisher
 */
public class ComponentScanParserWithUserDefinedStrategiesTests {

	@Test
	public void testCustomBeanNameGenerator() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/context/annotation/customNameGeneratorTests.xml");
		assertThat(context.containsBean("testing.fooServiceImpl")).isTrue();
	}

	@Test
	public void testCustomScopeMetadataResolver() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/context/annotation/customScopeResolverTests.xml");
		BeanDefinition bd = context.getBeanFactory().getBeanDefinition("fooServiceImpl");
		assertThat(bd.getScope()).isEqualTo("myCustomScope");
		assertThat(bd.isSingleton()).isFalse();
	}

	@Test
	public void testInvalidConstructorBeanNameGenerator() {
		assertThatExceptionOfType(BeansException.class).isThrownBy(() ->
			new ClassPathXmlApplicationContext(
					"org/springframework/context/annotation/invalidConstructorNameGeneratorTests.xml"));
	}

	@Test
	public void testInvalidClassNameScopeMetadataResolver() {
		assertThatExceptionOfType(BeansException.class).isThrownBy(() ->
				new ClassPathXmlApplicationContext(
						"org/springframework/context/annotation/invalidClassNameScopeResolverTests.xml"));
	}

}
