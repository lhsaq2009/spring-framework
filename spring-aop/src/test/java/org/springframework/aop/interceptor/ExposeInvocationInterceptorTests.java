package org.springframework.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.testfixture.beans.ITestBean;
import org.springframework.beans.testfixture.beans.TestBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.core.testfixture.io.ResourceTestUtils.qualifiedResource;

/**
 * Non-XML tests are in AbstractAopProxyTests
 *
 * @author Rod Johnson
 * @author Chris Beams
 */
public class ExposeInvocationInterceptorTests {

	@Test
	public void testXmlConfig() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(bf).loadBeanDefinitions(
				qualifiedResource(ExposeInvocationInterceptorTests.class, "context.xml"));
		ITestBean tb = (ITestBean) bf.getBean("proxy");
		String name = "tony";
		tb.setName(name);
		// Fires context checks
		assertThat(tb.getName()).isEqualTo(name);
	}

}


abstract class ExposedInvocationTestBean extends TestBean {

	@Override
	public String getName() {
		MethodInvocation invocation = ExposeInvocationInterceptor.currentInvocation();
		assertions(invocation);
		return super.getName();
	}

	@Override
	public void absquatulate() {
		MethodInvocation invocation = ExposeInvocationInterceptor.currentInvocation();
		assertions(invocation);
		super.absquatulate();
	}

	protected abstract void assertions(MethodInvocation invocation);
}


class InvocationCheckExposedInvocationTestBean extends ExposedInvocationTestBean {

	@Override
	protected void assertions(MethodInvocation invocation) {
		assertThat(invocation.getThis() == this).isTrue();
		assertThat(ITestBean.class.isAssignableFrom(invocation.getMethod().getDeclaringClass())).as("Invocation should be on ITestBean: " + invocation.getMethod()).isTrue();
	}
}
