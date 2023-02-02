package org.aopalliance.intercept;

/**
 * Intercepts calls on an interface on its way to the target. These
 * are nested "on top" of the target.<br/><br/>
 *
 * 拦截在到达目标的接口上的调用。它们嵌套在目标的 “顶部”。<br/><br/>
 *
 * <hr>
 *
 * <p>The user should implement the {@link #invoke(MethodInvocation)}
 * method to modify the original behavior. E.g. the following class
 * implements a tracing interceptor (traces all the calls on the
 * intercepted method(s)):<br/><br/>
 *
 * 用户应实现 invoke (Methodlnvocation）方法来修改原始行为。例如，以下类实现了一个
 * 跟踪拦截器（跟踪截获方法上的所有调用）<br/><br/>
 *
 * <hr>
 *
 * <pre class=code>
 * class TracingInterceptor implements MethodInterceptor {
 *   Object invoke(MethodInvocation i) throws Throwable {
 *     System.out.println("method "+i.getMethod()+" is called on "+
 *                        i.getThis()+" with args "+i.getArguments());
 *     Object ret=i.proceed();
 *     System.out.println("method "+i.getMethod()+" returns "+ret);
 *     return ret;
 *   }
 * }
 * </pre>
 *
 * @author Rod Johnson
 */
@FunctionalInterface
public interface MethodInterceptor extends Interceptor {		//

	/**
	 * Implement this method to perform extra treatments before and
	 * after the invocation. Polite implementations would certainly
	 * like to invoke {@link Joinpoint#proceed()}.<br/><br/>
	 *
	 * 实现此方法以在 调用前/后 执行额外的处理 ( extra treatments )。
	 * 礼貌的实现当然希望调用 {@link Joinpoint#proceed()}.<br/><br/>
	 *
	 * <hr>
	 *
	 * @param invocation the method invocation joinpoint
	 * @return the result of the call to {@link Joinpoint#proceed()};
	 * might be intercepted by the interceptor
	 * @throws Throwable if the interceptors or the target object
	 * throws an exception
	 */
	Object invoke(MethodInvocation invocation) throws Throwable;

}
