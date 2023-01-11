package org.springframework.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.testfixture.beans.CollectingReaderEventListener;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Torsten Juergeleit
 * @author Juergen Hoeller
 */
public class TxNamespaceHandlerEventTests {

	private DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

	private CollectingReaderEventListener eventListener = new CollectingReaderEventListener();


	@BeforeEach
	public void setUp() throws Exception {
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.beanFactory);
		reader.setEventListener(this.eventListener);
		reader.loadBeanDefinitions(new ClassPathResource("txNamespaceHandlerTests.xml", getClass()));
	}

	@Test
	public void componentEventReceived() {
		ComponentDefinition component = this.eventListener.getComponentDefinition("txAdvice");
		assertThat(component).isInstanceOf(BeanComponentDefinition.class);
	}

}
