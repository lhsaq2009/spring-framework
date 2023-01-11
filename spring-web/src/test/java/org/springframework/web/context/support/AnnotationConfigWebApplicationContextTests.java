package org.springframework.web.context.support;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Chris Beams
 * @author Juergen Hoeller
 */
public class AnnotationConfigWebApplicationContextTests {

	@Test
	@SuppressWarnings("resource")
	public void registerSingleClass() {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(Config.class);
		ctx.refresh();

		TestBean bean = ctx.getBean(TestBean.class);
		assertThat(bean).isNotNull();
	}

	@Test
	@SuppressWarnings("resource")
	public void configLocationWithSingleClass() {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.setConfigLocation(Config.class.getName());
		ctx.refresh();

		TestBean bean = ctx.getBean(TestBean.class);
		assertThat(bean).isNotNull();
	}

	@Test
	@SuppressWarnings("resource")
	public void configLocationWithBasePackage() {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.setConfigLocation("org.springframework.web.context.support");
		ctx.refresh();

		TestBean bean = ctx.getBean(TestBean.class);
		assertThat(bean).isNotNull();
	}

	@Test
	@SuppressWarnings("resource")
	public void withBeanNameGenerator() {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.setBeanNameGenerator(new AnnotationBeanNameGenerator() {
			@Override
			public String generateBeanName(BeanDefinition definition,
					BeanDefinitionRegistry registry) {
				return "custom-" + super.generateBeanName(definition, registry);
			}
		});
		ctx.setConfigLocation(Config.class.getName());
		ctx.refresh();
		assertThat(ctx.containsBean("custom-myConfig")).isTrue();
	}


	@Configuration("myConfig")
	static class Config {

		@Bean
		public TestBean myTestBean() {
			return new TestBean();
		}
	}


	static class TestBean {
	}

}
