package org.springframework.test.context.junit4.spr9051;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests that verify proper scoping of beans created in
 * <em>{@code @Bean} Lite Mode</em>.
 *
 * @author Sam Brannen
 * @since 3.2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AtBeanLiteModeScopeTests.LiteBeans.class)
public class AtBeanLiteModeScopeTests {

	/**
	 * This is intentionally <b>not</b> annotated with {@code @Configuration}.
	 */
	static class LiteBeans {

		@Bean
		public LifecycleBean singleton() {
			LifecycleBean bean = new LifecycleBean("singleton");
			assertThat(bean.isInitialized()).isFalse();
			return bean;
		}

		@Bean
		@Scope("prototype")
		public LifecycleBean prototype() {
			LifecycleBean bean = new LifecycleBean("prototype");
			assertThat(bean.isInitialized()).isFalse();
			return bean;
		}
	}


	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	@Qualifier("singleton")
	private LifecycleBean injectedSingletonBean;

	@Autowired
	@Qualifier("prototype")
	private LifecycleBean injectedPrototypeBean;


	@Test
	public void singletonLiteBean() {
		assertThat(injectedSingletonBean).isNotNull();
		assertThat(injectedSingletonBean.isInitialized()).isTrue();

		LifecycleBean retrievedSingletonBean = applicationContext.getBean("singleton", LifecycleBean.class);
		assertThat(retrievedSingletonBean).isNotNull();
		assertThat(retrievedSingletonBean.isInitialized()).isTrue();

		assertThat(retrievedSingletonBean).isSameAs(injectedSingletonBean);
	}

	@Test
	public void prototypeLiteBean() {
		assertThat(injectedPrototypeBean).isNotNull();
		assertThat(injectedPrototypeBean.isInitialized()).isTrue();

		LifecycleBean retrievedPrototypeBean = applicationContext.getBean("prototype", LifecycleBean.class);
		assertThat(retrievedPrototypeBean).isNotNull();
		assertThat(retrievedPrototypeBean.isInitialized()).isTrue();

		assertThat(retrievedPrototypeBean).isNotSameAs(injectedPrototypeBean);
	}

}
