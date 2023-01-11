package org.springframework.beans.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Written with the intention of reproducing SPR-7318.
 *
 * @author Chris Beams
 */
public class FactoryBeanLookupTests {
	private BeanFactory beanFactory;

	@BeforeEach
	public void setUp() {
		beanFactory = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader((BeanDefinitionRegistry) beanFactory).loadBeanDefinitions(
				new ClassPathResource("FactoryBeanLookupTests-context.xml", this.getClass()));
	}

	@Test
	public void factoryBeanLookupByNameDereferencing() {
		Object fooFactory = beanFactory.getBean("&fooFactory");
		assertThat(fooFactory).isInstanceOf(FooFactoryBean.class);
	}

	@Test
	public void factoryBeanLookupByType() {
		FooFactoryBean fooFactory = beanFactory.getBean(FooFactoryBean.class);
		assertThat(fooFactory).isNotNull();
	}

	@Test
	public void factoryBeanLookupByTypeAndNameDereference() {
		FooFactoryBean fooFactory = beanFactory.getBean("&fooFactory", FooFactoryBean.class);
		assertThat(fooFactory).isNotNull();
	}

	@Test
	public void factoryBeanObjectLookupByName() {
		Object fooFactory = beanFactory.getBean("fooFactory");
		assertThat(fooFactory).isInstanceOf(Foo.class);
	}

	@Test
	public void factoryBeanObjectLookupByNameAndType() {
		Foo foo = beanFactory.getBean("fooFactory", Foo.class);
		assertThat(foo).isNotNull();
	}
}

class FooFactoryBean extends AbstractFactoryBean<Foo> {
	@Override
	protected Foo createInstance() throws Exception {
		return new Foo();
	}

	@Override
	public Class<?> getObjectType() {
		return Foo.class;
	}
}

class Foo { }
