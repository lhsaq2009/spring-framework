package org.springframework.aop.config;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.core.testfixture.io.ResourceTestUtils.qualifiedResource;

/**
 * Tests that the &lt;aop:config/&gt; element can be used as a top level element.
 *
 * @author Rob Harrop
 * @author Chris Beams
 */
public class TopLevelAopTagTests {

	@Test
	public void testParse() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(beanFactory).loadBeanDefinitions(
				qualifiedResource(TopLevelAopTagTests.class, "context.xml"));

		assertThat(beanFactory.containsBeanDefinition("testPointcut")).isTrue();
	}

}
