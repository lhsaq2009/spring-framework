package org.springframework.beans.factory.serviceloader;

import java.util.List;
import java.util.ServiceLoader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 */
class ServiceLoaderTests {

	@BeforeAll
	static void assumeDocumentBuilderFactoryCanBeLoaded() {
		assumeTrue(ServiceLoader.load(DocumentBuilderFactory.class).iterator().hasNext());
	}

	@Test
	void testServiceLoaderFactoryBean() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition bd = new RootBeanDefinition(ServiceLoaderFactoryBean.class);
		bd.getPropertyValues().add("serviceType", DocumentBuilderFactory.class.getName());
		bf.registerBeanDefinition("service", bd);
		ServiceLoader<?> serviceLoader = (ServiceLoader<?>) bf.getBean("service");
		boolean condition = serviceLoader.iterator().next() instanceof DocumentBuilderFactory;
		assertThat(condition).isTrue();
	}

	@Test
	void testServiceFactoryBean() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition bd = new RootBeanDefinition(ServiceFactoryBean.class);
		bd.getPropertyValues().add("serviceType", DocumentBuilderFactory.class.getName());
		bf.registerBeanDefinition("service", bd);
		boolean condition = bf.getBean("service") instanceof DocumentBuilderFactory;
		assertThat(condition).isTrue();
	}

	@Test
	void testServiceListFactoryBean() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition bd = new RootBeanDefinition(ServiceListFactoryBean.class);
		bd.getPropertyValues().add("serviceType", DocumentBuilderFactory.class.getName());
		bf.registerBeanDefinition("service", bd);
		List<?> serviceList = (List<?>) bf.getBean("service");
		boolean condition = serviceList.get(0) instanceof DocumentBuilderFactory;
		assertThat(condition).isTrue();
	}

}
