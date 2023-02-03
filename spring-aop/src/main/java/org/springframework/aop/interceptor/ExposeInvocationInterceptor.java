package org.springframework.aop.interceptor;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.PriorityOrdered;

/**
 * 将当前 MethodInvocation 公开为 thread-local 对象的拦截器。
 * 我们偶尔需要这样做; 例如，当切入点（例如 AspectJ 表达式切入点）需要知道完整的调用上下文时。<br/><br/>
 *
 * 除非确实有必要，否则不要使用此拦截器。
 * 目标对象通常不应该知道 Spring AOP，因为这会创建对 Spring API 的依赖。目标对象应尽可能是普通的 POJO。<br/><br/>
 *
 * 如果使用，此拦截器通常是拦截器链中的第一个拦截器。<br/><br/>
 *
 * <hr><br/>
 *
 * Interceptor that exposes the current {@link org.aopalliance.intercept.MethodInvocation}
 * as a thread-local object. We occasionally need to do this; for example, when a pointcut
 * (e.g. an AspectJ expression pointcut) needs to know the full invocation context.
 *
 * <p>Don't use this interceptor unless this is really necessary. Target objects should
 * not normally know about Spring AOP, as this creates a dependency on Spring API.
 * Target objects should be plain POJOs as far as possible.
 *
 * <p>If used, this interceptor will normally be the first in the interceptor chain.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 *
 * ----------------------------------------------------------
 *
 *                             Advice
 *                               ▲
 *                        Interceptor           Ordered
 *                               ▲                 ▲
 *                  MethodInterceptor   PriorityOrdered
 *                               ▲                 ▲
 *                               │┌────────────────┘
 *        ExposeInvocationInterceptor
 */
@SuppressWarnings("serial")
public final class ExposeInvocationInterceptor implements MethodInterceptor, PriorityOrdered, Serializable {

	/** 此类的单一实例；Singleton instance of this class. */
	public static final ExposeInvocationInterceptor INSTANCE = new ExposeInvocationInterceptor();

	/**
	 * Singleton advisor for this class. Use in preference to INSTANCE when using
	 * Spring AOP, as it prevents the need to create a new Advisor to wrap the instance.<br/><br/>
	 *
	 * 本类的单例 advisor。
	 * 使用 Spring AOP 时，优先使用 INSTANCE，因为它无需创建新的 Advisor 来包装实例。<br/><br/>
	 */
	public static final Advisor ADVISOR = new DefaultPointcutAdvisor(INSTANCE) {		//
		@Override
		public String toString() {
			return ExposeInvocationInterceptor.class.getName() +".ADVISOR";
		}
	};

	private static final ThreadLocal<MethodInvocation> invocation =								//
			new NamedThreadLocal<>("Current AOP method invocation");						// 当前 AOP 方法调用

	/**
	 * Return the AOP Alliance MethodInvocation object associated with the current invocation.<br/><br/>
	 *
	 * 返回与当前调用关联的「AOP 联盟 MethodInvocation 对象」。<br/><br/>
	 *
	 * <hr>
	 *
	 * @return the invocation object associated with the current invocation
	 * @throws IllegalStateException if there is no AOP invocation in progress,
	 * or if the ExposeInvocationInterceptor was not added to this interceptor chain
	 */
	public static MethodInvocation currentInvocation() throws IllegalStateException {			//
		MethodInvocation mi = invocation.get();													//
		if (mi == null) {
			throw new IllegalStateException(
					"No MethodInvocation found: Check that an AOP invocation is in progress and that the " +
					"ExposeInvocationInterceptor is upfront in the interceptor chain. Specifically, note that " +
					"advices with order HIGHEST_PRECEDENCE will execute before ExposeInvocationInterceptor! " +
					"In addition, ExposeInvocationInterceptor and ExposeInvocationInterceptor.currentInvocation() " +
					"must be invoked from the same thread.");
		}
		return mi;
	}


	/**
	 * Ensures that only the canonical instance can be created.
	 */
	private ExposeInvocationInterceptor() {
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		MethodInvocation oldInvocation = invocation.get();
		invocation.set(mi);
		try {
			return mi.proceed();			// TODO：有何妙用，AOP 再分析吃透，2023-01-13
		}
		finally {
			invocation.set(oldInvocation);
		}
	}

	@Override
	public int getOrder() {
		return PriorityOrdered.HIGHEST_PRECEDENCE + 1;
	}

	/**
	 * Required to support serialization. Replaces with canonical instance
	 * on deserialization, protecting Singleton pattern.
	 * <p>Alternative to overriding the {@code equals} method.
	 */
	private Object readResolve() {
		return INSTANCE;
	}

}
