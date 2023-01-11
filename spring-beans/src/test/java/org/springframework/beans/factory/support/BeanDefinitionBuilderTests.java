package org.springframework.beans.factory.support;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.testfixture.beans.TestBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public class BeanDefinitionBuilderTests {

	@Test
	public void beanClassWithSimpleProperty() {
		String[] dependsOn = new String[] { "A", "B", "C" };
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(TestBean.class);
		bdb.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		bdb.addPropertyValue("age", "15");
		for (String dependsOnEntry : dependsOn) {
			bdb.addDependsOn(dependsOnEntry);
		}

		RootBeanDefinition rbd = (RootBeanDefinition) bdb.getBeanDefinition();
		assertThat(rbd.isSingleton()).isFalse();
		assertThat(rbd.getBeanClass()).isEqualTo(TestBean.class);
		assertThat(Arrays.equals(dependsOn, rbd.getDependsOn())).as("Depends on was added").isTrue();
		assertThat(rbd.getPropertyValues().contains("age")).isTrue();
	}

	@Test
	public void beanClassWithFactoryMethod() {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(TestBean.class, "create");
		RootBeanDefinition rbd = (RootBeanDefinition) bdb.getBeanDefinition();
		assertThat(rbd.hasBeanClass()).isTrue();
		assertThat(rbd.getBeanClass()).isEqualTo(TestBean.class);
		assertThat(rbd.getFactoryMethodName()).isEqualTo("create");
	}

	@Test
	public void beanClassName() {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(TestBean.class.getName());
		RootBeanDefinition rbd = (RootBeanDefinition) bdb.getBeanDefinition();
		assertThat(rbd.hasBeanClass()).isFalse();
		assertThat(rbd.getBeanClassName()).isEqualTo(TestBean.class.getName());
	}

	@Test
	public void beanClassNameWithFactoryMethod() {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(TestBean.class.getName(), "create");
		RootBeanDefinition rbd = (RootBeanDefinition) bdb.getBeanDefinition();
		assertThat(rbd.hasBeanClass()).isFalse();
		assertThat(rbd.getBeanClassName()).isEqualTo(TestBean.class.getName());
		assertThat(rbd.getFactoryMethodName()).isEqualTo("create");
	}

}
