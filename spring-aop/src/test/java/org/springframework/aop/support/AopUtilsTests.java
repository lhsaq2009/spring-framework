package org.springframework.aop.support;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.target.EmptyTargetSource;
import org.springframework.aop.testfixture.interceptor.NopInterceptor;
import org.springframework.beans.testfixture.beans.TestBean;
import org.springframework.core.testfixture.io.SerializationTestUtils;
import org.springframework.lang.Nullable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rod Johnson
 * @author Chris Beams
 */
public class AopUtilsTests {

	@Test
	public void testPointcutCanNeverApply() {
		class TestPointcut extends StaticMethodMatcherPointcut {
			@Override
			public boolean matches(Method method, @Nullable Class<?> clazzy) {
				return false;
			}
		}

		Pointcut no = new TestPointcut();
		assertThat(AopUtils.canApply(no, Object.class)).isFalse();
	}

	@Test
	public void testPointcutAlwaysApplies() {
		assertThat(AopUtils.canApply(new DefaultPointcutAdvisor(new NopInterceptor()), Object.class)).isTrue();
		assertThat(AopUtils.canApply(new DefaultPointcutAdvisor(new NopInterceptor()), TestBean.class)).isTrue();
	}

	@Test
	public void testPointcutAppliesToOneMethodOnObject() {
		class TestPointcut extends StaticMethodMatcherPointcut {
			@Override
			public boolean matches(Method method, @Nullable Class<?> clazz) {
				return method.getName().equals("hashCode");
			}
		}

		Pointcut pc = new TestPointcut();

		// will return true if we're not proxying interfaces
		assertThat(AopUtils.canApply(pc, Object.class)).isTrue();
	}

	/**
	 * Test that when we serialize and deserialize various canonical instances
	 * of AOP classes, they return the same instance, not a new instance
	 * that's subverted the singleton construction limitation.
	 */
	@Test
	public void testCanonicalFrameworkClassesStillCanonicalOnDeserialization() throws Exception {
		assertThat(SerializationTestUtils.serializeAndDeserialize(MethodMatcher.TRUE)).isSameAs(MethodMatcher.TRUE);
		assertThat(SerializationTestUtils.serializeAndDeserialize(ClassFilter.TRUE)).isSameAs(ClassFilter.TRUE);
		assertThat(SerializationTestUtils.serializeAndDeserialize(Pointcut.TRUE)).isSameAs(Pointcut.TRUE);
		assertThat(SerializationTestUtils.serializeAndDeserialize(EmptyTargetSource.INSTANCE)).isSameAs(EmptyTargetSource.INSTANCE);
		assertThat(SerializationTestUtils.serializeAndDeserialize(Pointcuts.SETTERS)).isSameAs(Pointcuts.SETTERS);
		assertThat(SerializationTestUtils.serializeAndDeserialize(Pointcuts.GETTERS)).isSameAs(Pointcuts.GETTERS);
		assertThat(SerializationTestUtils.serializeAndDeserialize(ExposeInvocationInterceptor.INSTANCE)).isSameAs(ExposeInvocationInterceptor.INSTANCE);
	}

}
