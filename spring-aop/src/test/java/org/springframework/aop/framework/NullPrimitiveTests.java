package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.Test;

import org.springframework.aop.AopInvocationException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Test for SPR-4675. A null value returned from around advice is very hard to debug if
 * the caller expects a primitive.
 *
 * @author Dave Syer
 */
public class NullPrimitiveTests {

	interface Foo {
		int getValue();
	}

	@Test
	public void testNullPrimitiveWithJdkProxy() {

		class SimpleFoo implements Foo {
			@Override
			public int getValue() {
				return 100;
			}
		}

		SimpleFoo target = new SimpleFoo();
		ProxyFactory factory = new ProxyFactory(target);
		factory.addAdvice((MethodInterceptor) invocation -> null);

		Foo foo = (Foo) factory.getProxy();

		assertThatExceptionOfType(AopInvocationException.class).isThrownBy(() ->
				foo.getValue())
			.withMessageContaining("Foo.getValue()");
	}

	public static class Bar {
		public int getValue() {
			return 100;
		}
	}

	@Test
	public void testNullPrimitiveWithCglibProxy() {

		Bar target = new Bar();
		ProxyFactory factory = new ProxyFactory(target);
		factory.addAdvice((MethodInterceptor) invocation -> null);

		Bar bar = (Bar) factory.getProxy();

		assertThatExceptionOfType(AopInvocationException.class).isThrownBy(() ->
				bar.getValue())
			.withMessageContaining("Bar.getValue()");
	}

}
