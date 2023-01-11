package org.springframework.aop.scope;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.core.testfixture.io.ResourceTestUtils.qualifiedResource;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class ScopedProxyAutowireTests {

	@Test
	public void testScopedProxyInheritsAutowireCandidateFalse() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(bf).loadBeanDefinitions(
				qualifiedResource(ScopedProxyAutowireTests.class, "scopedAutowireFalse.xml"));

		assertThat(Arrays.asList(bf.getBeanNamesForType(TestBean.class, false, false)).contains("scoped")).isTrue();
		assertThat(Arrays.asList(bf.getBeanNamesForType(TestBean.class, true, false)).contains("scoped")).isTrue();
		assertThat(bf.containsSingleton("scoped")).isFalse();
		TestBean autowired = (TestBean) bf.getBean("autowired");
		TestBean unscoped = (TestBean) bf.getBean("unscoped");
		assertThat(autowired.getChild()).isSameAs(unscoped);
	}

	@Test
	public void testScopedProxyReplacesAutowireCandidateTrue() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(bf).loadBeanDefinitions(
				qualifiedResource(ScopedProxyAutowireTests.class, "scopedAutowireTrue.xml"));

		assertThat(Arrays.asList(bf.getBeanNamesForType(TestBean.class, true, false)).contains("scoped")).isTrue();
		assertThat(Arrays.asList(bf.getBeanNamesForType(TestBean.class, false, false)).contains("scoped")).isTrue();
		assertThat(bf.containsSingleton("scoped")).isFalse();
		TestBean autowired = (TestBean) bf.getBean("autowired");
		TestBean scoped = (TestBean) bf.getBean("scoped");
		assertThat(autowired.getChild()).isSameAs(scoped);
	}


	static class TestBean {

		private TestBean child;

		public void setChild(TestBean child) {
			this.child = child;
		}

		public TestBean getChild() {
			return this.child;
		}
	}

}
