package org.springframework.beans.factory.support;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Unit tests for SPR-8954, in which a custom {@link InstantiationAwareBeanPostProcessor}
 * forces the predicted type of a FactoryBean, effectively preventing retrieval of the
 * bean from calls to #getBeansOfType(FactoryBean.class). The implementation of
 * {@link AbstractBeanFactory#isFactoryBean(String, RootBeanDefinition)} now ensures that
 * not only the predicted bean type is considered, but also the original bean definition's
 * beanClass.
 *
 * @author Chris Beams
 * @author Oliver Gierke
 */
public class Spr8954Tests {

	private DefaultListableBeanFactory bf;

	@BeforeEach
	public void setUp() {
		bf = new DefaultListableBeanFactory();
		bf.registerBeanDefinition("foo", new RootBeanDefinition(FooFactoryBean.class));
		bf.addBeanPostProcessor(new PredictingBPP());
	}

	@Test
	public void repro() {
		assertThat(bf.getBean("foo")).isInstanceOf(Foo.class);
		assertThat(bf.getBean("&foo")).isInstanceOf(FooFactoryBean.class);
		assertThat(bf.isTypeMatch("&foo", FactoryBean.class)).isTrue();

		@SuppressWarnings("rawtypes")
		Map<String, FactoryBean> fbBeans = bf.getBeansOfType(FactoryBean.class);
		assertThat(fbBeans).hasSize(1);
		assertThat(fbBeans.keySet()).contains("&foo");

		Map<String, AnInterface> aiBeans = bf.getBeansOfType(AnInterface.class);
		assertThat(aiBeans).hasSize(1);
		assertThat(aiBeans.keySet()).contains("&foo");
	}

	@Test
	public void findsBeansByTypeIfNotInstantiated() {
		assertThat(bf.isTypeMatch("&foo", FactoryBean.class)).isTrue();

		@SuppressWarnings("rawtypes")
		Map<String, FactoryBean> fbBeans = bf.getBeansOfType(FactoryBean.class);
		assertThat(1).isEqualTo(fbBeans.size());
		assertThat("&foo").isEqualTo(fbBeans.keySet().iterator().next());

		Map<String, AnInterface> aiBeans = bf.getBeansOfType(AnInterface.class);
		assertThat(aiBeans).hasSize(1);
		assertThat(aiBeans.keySet()).contains("&foo");
	}

	/**
	 * SPR-10517
	 */
	@Test
	public void findsFactoryBeanNameByTypeWithoutInstantiation() {
		String[] names = bf.getBeanNamesForType(AnInterface.class, false, false);
		assertThat(Arrays.asList(names)).contains("&foo");

		Map<String, AnInterface> beans = bf.getBeansOfType(AnInterface.class, false, false);
		assertThat(beans).hasSize(1);
		assertThat(beans.keySet()).contains("&foo");
	}


	static class FooFactoryBean implements FactoryBean<Foo>, AnInterface {

		@Override
		public Foo getObject() throws Exception {
			return new Foo();
		}

		@Override
		public Class<?> getObjectType() {
			return Foo.class;
		}

		@Override
		public boolean isSingleton() {
			return true;
		}
	}

	interface AnInterface {
	}

	static class Foo {
	}

	interface PredictedType {
	}

	static class PredictedTypeImpl implements PredictedType {
	}

	static class PredictingBPP extends InstantiationAwareBeanPostProcessorAdapter {

		@Override
		public Class<?> predictBeanType(Class<?> beanClass, String beanName) {
			return FactoryBean.class.isAssignableFrom(beanClass) ? PredictedType.class : null;
		}
	}

}
