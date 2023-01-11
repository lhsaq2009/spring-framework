package org.springframework.context.annotation;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.AnnotationMetadata;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link AbstractBeanFactory} type inference from
 * {@link FactoryBean FactoryBeans} defined in the configuration.
 *
 * @author Phillip Webb
 */
public class ConfigurationWithFactoryBeanBeanEarlyDeductionTests {

	@Test
	public void preFreezeDirect() {
		assertPreFreeze(DirectConfiguration.class);
	}

	@Test
	public void postFreezeDirect() {
		assertPostFreeze(DirectConfiguration.class);
	}

	@Test
	public void preFreezeGenericMethod() {
		assertPreFreeze(GenericMethodConfiguration.class);
	}

	@Test
	public void postFreezeGenericMethod() {
		assertPostFreeze(GenericMethodConfiguration.class);
	}

	@Test
	public void preFreezeGenericClass() {
		assertPreFreeze(GenericClassConfiguration.class);
	}

	@Test
	public void postFreezeGenericClass() {
		assertPostFreeze(GenericClassConfiguration.class);
	}

	@Test
	public void preFreezeAttribute() {
		assertPreFreeze(AttributeClassConfiguration.class);
	}

	@Test
	public void postFreezeAttribute() {
		assertPostFreeze(AttributeClassConfiguration.class);
	}

	@Test
	public void preFreezeUnresolvedGenericFactoryBean() {
		// Covers the case where a @Configuration is picked up via component scanning
		// and its bean definition only has a String bean class. In such cases
		// beanDefinition.hasBeanClass() returns false so we need to actually
		// call determineTargetType ourselves
		GenericBeanDefinition factoryBeanDefinition = new GenericBeanDefinition();
		factoryBeanDefinition.setBeanClassName(GenericClassConfiguration.class.getName());
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(FactoryBean.class);
		beanDefinition.setFactoryBeanName("factoryBean");
		beanDefinition.setFactoryMethodName("myBean");
		GenericApplicationContext context = new GenericApplicationContext();
		try {
			context.registerBeanDefinition("factoryBean", factoryBeanDefinition);
			context.registerBeanDefinition("myBean", beanDefinition);
			NameCollectingBeanFactoryPostProcessor postProcessor = new NameCollectingBeanFactoryPostProcessor();
			context.addBeanFactoryPostProcessor(postProcessor);
			context.refresh();
			assertContainsMyBeanName(postProcessor.getNames());
		}
		finally {
			context.close();
		}
	}

	private void assertPostFreeze(Class<?> configurationClass) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				configurationClass);
		assertContainsMyBeanName(context);
	}

	private void assertPreFreeze(Class<?> configurationClass,
			BeanFactoryPostProcessor... postProcessors) {
		NameCollectingBeanFactoryPostProcessor postProcessor = new NameCollectingBeanFactoryPostProcessor();
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		try {
			Arrays.stream(postProcessors).forEach(context::addBeanFactoryPostProcessor);
			context.addBeanFactoryPostProcessor(postProcessor);
			context.register(configurationClass);
			context.refresh();
			assertContainsMyBeanName(postProcessor.getNames());
		}
		finally {
			context.close();
		}
	}

	private void assertContainsMyBeanName(AnnotationConfigApplicationContext context) {
		assertContainsMyBeanName(context.getBeanNamesForType(MyBean.class, true, false));
	}

	private void assertContainsMyBeanName(String[] names) {
		assertThat(names).containsExactly("myBean");
	}

	private static class NameCollectingBeanFactoryPostProcessor
			implements BeanFactoryPostProcessor {

		private String[] names;

		@Override
		public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
				throws BeansException {
			this.names = beanFactory.getBeanNamesForType(MyBean.class, true, false);
		}

		public String[] getNames() {
			return this.names;
		}

	}

	@Configuration
	static class DirectConfiguration {

		@Bean
		MyBean myBean() {
			return new MyBean();
		}

	}

	@Configuration
	static class GenericMethodConfiguration {

		@Bean
		FactoryBean<MyBean> myBean() {
			return new TestFactoryBean<>(new MyBean());
		}

	}

	@Configuration
	static class GenericClassConfiguration {

		@Bean
		MyFactoryBean myBean() {
			return new MyFactoryBean();
		}

	}

	@Configuration
	@Import(AttributeClassRegistrar.class)
	static class AttributeClassConfiguration {

	}

	static class AttributeClassRegistrar implements ImportBeanDefinitionRegistrar {

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
			BeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(
					RawWithAbstractObjectTypeFactoryBean.class).getBeanDefinition();
			definition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, MyBean.class);
			registry.registerBeanDefinition("myBean", definition);
		}

	}

	abstract static class AbstractMyBean {
	}

	static class MyBean extends AbstractMyBean {
	}

	static class TestFactoryBean<T> implements FactoryBean<T> {

		private final T instance;

		public TestFactoryBean(T instance) {
			this.instance = instance;
		}

		@Override
		public T getObject() throws Exception {
			return this.instance;
		}

		@Override
		public Class<?> getObjectType() {
			return this.instance.getClass();
		}

	}

	static class MyFactoryBean extends TestFactoryBean<MyBean> {

		public MyFactoryBean() {
			super(new MyBean());
		}

	}

	static class RawWithAbstractObjectTypeFactoryBean implements FactoryBean<Object> {

		private final Object object = new MyBean();

		@Override
		public Object getObject() throws Exception {
			return object;
		}

		@Override
		public Class<?> getObjectType() {
			return MyBean.class;
		}

	}

}
