package org.springframework.aop.config;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.core.testfixture.io.ResourceTestUtils.qualifiedResource;

/**
 * @author Mark Fisher
 * @author Chris Beams
 */
public class AopNamespaceHandlerPointcutErrorTests {

	@Test
	public void testDuplicatePointcutConfig() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		assertThatExceptionOfType(BeanDefinitionStoreException.class).isThrownBy(() ->
				new XmlBeanDefinitionReader(bf).loadBeanDefinitions(
						qualifiedResource(getClass(), "pointcutDuplication.xml")))
			.satisfies(ex -> ex.contains(BeanDefinitionParsingException.class));
	}

	@Test
	public void testMissingPointcutConfig() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		assertThatExceptionOfType(BeanDefinitionStoreException.class).isThrownBy(() ->
				new XmlBeanDefinitionReader(bf).loadBeanDefinitions(
						qualifiedResource(getClass(), "pointcutMissing.xml")))
			.satisfies(ex -> ex.contains(BeanDefinitionParsingException.class));
	}

}
