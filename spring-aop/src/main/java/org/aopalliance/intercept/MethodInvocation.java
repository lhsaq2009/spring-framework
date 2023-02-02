package org.aopalliance.intercept;

import java.lang.reflect.Method;

/**
 * 对方法的调用的描述，在 method-call 时提供给 interceptor。
 * 方法调用是一个连接点，可以由 method interceptor 拦截。<br/><br/>
 *
 * <hr><br/>
 *
 * Description of an invocation to a method, given to an interceptor upon method-call.
 *
 * <p>A method invocation is a joinpoint and can be intercepted by a
 * method interceptor.
 *
 * @author Rod Johnson
 * @see MethodInterceptor
 */
public interface MethodInvocation extends Invocation {

	/**
	 * 获取正在调用的方法。
	 * 此方法是 {@link Joinpoint#getStaticPart()} 的友好实现（相同的结果）。<br/><br/>
	 *
	 * <hr><br/>
	 *
	 * Get the method being called.
	 * <p>This method is a friendly implementation of the
	 * {@link Joinpoint#getStaticPart()} method (same result).
	 * @return the method being called
	 */
	Method getMethod();

}
