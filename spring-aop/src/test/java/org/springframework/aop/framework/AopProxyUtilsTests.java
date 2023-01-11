package org.springframework.aop.framework;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.aop.SpringProxy;
import org.springframework.beans.testfixture.beans.ITestBean;
import org.springframework.beans.testfixture.beans.TestBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author Rod Johnson
 * @author Chris Beams
 */
public class AopProxyUtilsTests {

	@Test
	public void testCompleteProxiedInterfacesWorksWithNull() {
		AdvisedSupport as = new AdvisedSupport();
		Class<?>[] completedInterfaces = AopProxyUtils.completeProxiedInterfaces(as);
		assertThat(completedInterfaces.length).isEqualTo(2);
		List<?> ifaces = Arrays.asList(completedInterfaces);
		assertThat(ifaces.contains(Advised.class)).isTrue();
		assertThat(ifaces.contains(SpringProxy.class)).isTrue();
	}

	@Test
	public void testCompleteProxiedInterfacesWorksWithNullOpaque() {
		AdvisedSupport as = new AdvisedSupport();
		as.setOpaque(true);
		Class<?>[] completedInterfaces = AopProxyUtils.completeProxiedInterfaces(as);
		assertThat(completedInterfaces.length).isEqualTo(1);
	}

	@Test
	public void testCompleteProxiedInterfacesAdvisedNotIncluded() {
		AdvisedSupport as = new AdvisedSupport();
		as.addInterface(ITestBean.class);
		as.addInterface(Comparable.class);
		Class<?>[] completedInterfaces = AopProxyUtils.completeProxiedInterfaces(as);
		assertThat(completedInterfaces.length).isEqualTo(4);

		// Can't assume ordering for others, so use a list
		List<?> l = Arrays.asList(completedInterfaces);
		assertThat(l.contains(Advised.class)).isTrue();
		assertThat(l.contains(ITestBean.class)).isTrue();
		assertThat(l.contains(Comparable.class)).isTrue();
	}

	@Test
	public void testCompleteProxiedInterfacesAdvisedIncluded() {
		AdvisedSupport as = new AdvisedSupport();
		as.addInterface(ITestBean.class);
		as.addInterface(Comparable.class);
		as.addInterface(Advised.class);
		Class<?>[] completedInterfaces = AopProxyUtils.completeProxiedInterfaces(as);
		assertThat(completedInterfaces.length).isEqualTo(4);

		// Can't assume ordering for others, so use a list
		List<?> l = Arrays.asList(completedInterfaces);
		assertThat(l.contains(Advised.class)).isTrue();
		assertThat(l.contains(ITestBean.class)).isTrue();
		assertThat(l.contains(Comparable.class)).isTrue();
	}

	@Test
	public void testCompleteProxiedInterfacesAdvisedNotIncludedOpaque() {
		AdvisedSupport as = new AdvisedSupport();
		as.setOpaque(true);
		as.addInterface(ITestBean.class);
		as.addInterface(Comparable.class);
		Class<?>[] completedInterfaces = AopProxyUtils.completeProxiedInterfaces(as);
		assertThat(completedInterfaces.length).isEqualTo(3);

		// Can't assume ordering for others, so use a list
		List<?> l = Arrays.asList(completedInterfaces);
		assertThat(l.contains(Advised.class)).isFalse();
		assertThat(l.contains(ITestBean.class)).isTrue();
		assertThat(l.contains(Comparable.class)).isTrue();
	}

	@Test
	public void testProxiedUserInterfacesWithSingleInterface() {
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(new TestBean());
		pf.addInterface(ITestBean.class);
		Object proxy = pf.getProxy();
		Class<?>[] userInterfaces = AopProxyUtils.proxiedUserInterfaces(proxy);
		assertThat(userInterfaces.length).isEqualTo(1);
		assertThat(userInterfaces[0]).isEqualTo(ITestBean.class);
	}

	@Test
	public void testProxiedUserInterfacesWithMultipleInterfaces() {
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(new TestBean());
		pf.addInterface(ITestBean.class);
		pf.addInterface(Comparable.class);
		Object proxy = pf.getProxy();
		Class<?>[] userInterfaces = AopProxyUtils.proxiedUserInterfaces(proxy);
		assertThat(userInterfaces.length).isEqualTo(2);
		assertThat(userInterfaces[0]).isEqualTo(ITestBean.class);
		assertThat(userInterfaces[1]).isEqualTo(Comparable.class);
	}

	@Test
	public void testProxiedUserInterfacesWithNoInterface() {
		Object proxy = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[0],
				(proxy1, method, args) -> null);
		assertThatIllegalArgumentException().isThrownBy(() ->
				AopProxyUtils.proxiedUserInterfaces(proxy));
	}

}
