package org.springframework.beans.factory.annotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.testfixture.beans.TestBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Karl Pietrzak
 * @author Juergen Hoeller
 */
public class LookupAnnotationTests {

	private DefaultListableBeanFactory beanFactory;


	@BeforeEach
	public void setup() {
		beanFactory = new DefaultListableBeanFactory();
		AutowiredAnnotationBeanPostProcessor aabpp = new AutowiredAnnotationBeanPostProcessor();
		aabpp.setBeanFactory(beanFactory);
		beanFactory.addBeanPostProcessor(aabpp);
		beanFactory.registerBeanDefinition("abstractBean", new RootBeanDefinition(AbstractBean.class));
		beanFactory.registerBeanDefinition("beanConsumer", new RootBeanDefinition(BeanConsumer.class));
		RootBeanDefinition tbd = new RootBeanDefinition(TestBean.class);
		tbd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		beanFactory.registerBeanDefinition("testBean", tbd);
	}


	@Test
	public void testWithoutConstructorArg() {
		AbstractBean bean = (AbstractBean) beanFactory.getBean("abstractBean");
		assertThat(bean).isNotNull();
		Object expected = bean.get();
		assertThat(expected.getClass()).isEqualTo(TestBean.class);
		assertThat(beanFactory.getBean(BeanConsumer.class).abstractBean).isSameAs(bean);
	}

	@Test
	public void testWithOverloadedArg() {
		AbstractBean bean = (AbstractBean) beanFactory.getBean("abstractBean");
		assertThat(bean).isNotNull();
		TestBean expected = bean.get("haha");
		assertThat(expected.getClass()).isEqualTo(TestBean.class);
		assertThat(expected.getName()).isEqualTo("haha");
		assertThat(beanFactory.getBean(BeanConsumer.class).abstractBean).isSameAs(bean);
	}

	@Test
	public void testWithOneConstructorArg() {
		AbstractBean bean = (AbstractBean) beanFactory.getBean("abstractBean");
		assertThat(bean).isNotNull();
		TestBean expected = bean.getOneArgument("haha");
		assertThat(expected.getClass()).isEqualTo(TestBean.class);
		assertThat(expected.getName()).isEqualTo("haha");
		assertThat(beanFactory.getBean(BeanConsumer.class).abstractBean).isSameAs(bean);
	}

	@Test
	public void testWithTwoConstructorArg() {
		AbstractBean bean = (AbstractBean) beanFactory.getBean("abstractBean");
		assertThat(bean).isNotNull();
		TestBean expected = bean.getTwoArguments("haha", 72);
		assertThat(expected.getClass()).isEqualTo(TestBean.class);
		assertThat(expected.getName()).isEqualTo("haha");
		assertThat(expected.getAge()).isEqualTo(72);
		assertThat(beanFactory.getBean(BeanConsumer.class).abstractBean).isSameAs(bean);
	}

	@Test
	public void testWithThreeArgsShouldFail() {
		AbstractBean bean = (AbstractBean) beanFactory.getBean("abstractBean");
		assertThat(bean).isNotNull();
		assertThatExceptionOfType(AbstractMethodError.class).as("TestBean has no three arg constructor").isThrownBy(() ->
				bean.getThreeArguments("name", 1, 2));
		assertThat(beanFactory.getBean(BeanConsumer.class).abstractBean).isSameAs(bean);
	}

	@Test
	public void testWithEarlyInjection() {
		AbstractBean bean = beanFactory.getBean("beanConsumer", BeanConsumer.class).abstractBean;
		assertThat(bean).isNotNull();
		Object expected = bean.get();
		assertThat(expected.getClass()).isEqualTo(TestBean.class);
		assertThat(beanFactory.getBean(BeanConsumer.class).abstractBean).isSameAs(bean);
	}

	@Test  // gh-25806
	public void testWithNullBean() {
		RootBeanDefinition tbd = new RootBeanDefinition(TestBean.class, () -> null);
		tbd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		beanFactory.registerBeanDefinition("testBean", tbd);

		AbstractBean bean = beanFactory.getBean("beanConsumer", BeanConsumer.class).abstractBean;
		assertThat(bean).isNotNull();
		Object expected = bean.get();
		assertThat(expected).isNull();
		assertThat(beanFactory.getBean(BeanConsumer.class).abstractBean).isSameAs(bean);
	}


	public static abstract class AbstractBean {

		@Lookup("testBean")
		public abstract TestBean get();

		@Lookup
		public abstract TestBean get(String name);  // overloaded

		@Lookup
		public abstract TestBean getOneArgument(String name);

		@Lookup
		public abstract TestBean getTwoArguments(String name, int age);

		// no @Lookup annotation
		public abstract TestBean getThreeArguments(String name, int age, int anotherArg);
	}


	public static class BeanConsumer {

		@Autowired
		AbstractBean abstractBean;
	}

}
