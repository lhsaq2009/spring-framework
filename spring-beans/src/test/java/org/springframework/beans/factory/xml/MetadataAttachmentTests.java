package org.springframework.beans.factory.xml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Harrop
 */
public class MetadataAttachmentTests {

	private DefaultListableBeanFactory beanFactory;


	@BeforeEach
	public void setUp() throws Exception {
		this.beanFactory = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(this.beanFactory).loadBeanDefinitions(
				new ClassPathResource("withMeta.xml", getClass()));
	}

	@Test
	public void metadataAttachment() throws Exception {
		BeanDefinition beanDefinition1 = this.beanFactory.getMergedBeanDefinition("testBean1");
		assertThat(beanDefinition1.getAttribute("foo")).isEqualTo("bar");
	}

	@Test
	public void metadataIsInherited() throws Exception {
		BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("testBean2");
		assertThat(beanDefinition.getAttribute("foo")).as("Metadata not inherited").isEqualTo("bar");
		assertThat(beanDefinition.getAttribute("abc")).as("Child metdata not attached").isEqualTo("123");
	}

	@Test
	public void propertyMetadata() throws Exception {
		BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("testBean3");
		PropertyValue pv = beanDefinition.getPropertyValues().getPropertyValue("name");
		assertThat(pv.getAttribute("surname")).isEqualTo("Harrop");
	}

}
