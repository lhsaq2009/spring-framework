package org.springframework.beans.factory.xml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class BeanNameGenerationTests {

	private DefaultListableBeanFactory beanFactory;


	@BeforeEach
	public void setUp() {
		this.beanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.beanFactory);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		reader.loadBeanDefinitions(new ClassPathResource("beanNameGeneration.xml", getClass()));
	}

	@Test
	public void naming() {
		String className = GeneratedNameBean.class.getName();

		String targetName = className + BeanDefinitionReaderUtils.GENERATED_BEAN_NAME_SEPARATOR + "0";
		GeneratedNameBean topLevel1 = (GeneratedNameBean) beanFactory.getBean(targetName);
		assertThat(topLevel1).isNotNull();

		targetName = className + BeanDefinitionReaderUtils.GENERATED_BEAN_NAME_SEPARATOR + "1";
		GeneratedNameBean topLevel2 = (GeneratedNameBean) beanFactory.getBean(targetName);
		assertThat(topLevel2).isNotNull();

		GeneratedNameBean child1 = topLevel1.getChild();
		assertThat(child1.getBeanName()).isNotNull();
		assertThat(child1.getBeanName().startsWith(className)).isTrue();

		GeneratedNameBean child2 = topLevel2.getChild();
		assertThat(child2.getBeanName()).isNotNull();
		assertThat(child2.getBeanName().startsWith(className)).isTrue();

		assertThat(child1.getBeanName().equals(child2.getBeanName())).isFalse();
	}

}
