package org.springframework.aop.interceptor;

import org.junit.jupiter.api.Test;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.NamedBean;
import org.springframework.beans.testfixture.beans.ITestBean;
import org.springframework.beans.testfixture.beans.TestBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rod Johnson
 * @author Chris Beams
 */
public class ExposeBeanNameAdvisorsTests {

	private class RequiresBeanNameBoundTestBean extends TestBean {
		private final String beanName;

		public RequiresBeanNameBoundTestBean(String beanName) {
			this.beanName = beanName;
		}

		@Override
		public int getAge() {
			assertThat(ExposeBeanNameAdvisors.getBeanName()).isEqualTo(beanName);
			return super.getAge();
		}
	}

	@Test
	public void testNoIntroduction() {
		String beanName = "foo";
		TestBean target = new RequiresBeanNameBoundTestBean(beanName);
		ProxyFactory pf = new ProxyFactory(target);
		pf.addAdvisor(ExposeInvocationInterceptor.ADVISOR);
		pf.addAdvisor(ExposeBeanNameAdvisors.createAdvisorWithoutIntroduction(beanName));
		ITestBean proxy = (ITestBean) pf.getProxy();

		boolean condition = proxy instanceof NamedBean;
		assertThat(condition).as("No introduction").isFalse();
		// Requires binding
		proxy.getAge();
	}

	@Test
	public void testWithIntroduction() {
		String beanName = "foo";
		TestBean target = new RequiresBeanNameBoundTestBean(beanName);
		ProxyFactory pf = new ProxyFactory(target);
		pf.addAdvisor(ExposeInvocationInterceptor.ADVISOR);
		pf.addAdvisor(ExposeBeanNameAdvisors.createAdvisorIntroducingNamedBean(beanName));
		ITestBean proxy = (ITestBean) pf.getProxy();

		boolean condition = proxy instanceof NamedBean;
		assertThat(condition).as("Introduction was made").isTrue();
		// Requires binding
		proxy.getAge();

		NamedBean nb = (NamedBean) proxy;
		assertThat(nb.getBeanName()).as("Name returned correctly").isEqualTo(beanName);
	}

}
