package org.springframework.aop.aspectj;

import java.util.List;

import org.springframework.aop.Advisor;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;

/**
 * Utility methods for working with AspectJ proxies.
 *
 * @author Rod Johnson
 * @author Ramnivas Laddad
 * @author Juergen Hoeller
 * @since 2.0
 */
public abstract class AspectJProxyUtils {		//

	/**
	 * Add special advisors if necessary to work with a proxy chain that contains AspectJ advisors:
	 * concretely, {@link ExposeInvocationInterceptor} at the beginning of the list.<br/><br/>
	 *
	 * <p>This will expose the current Spring AOP invocation (necessary for some AspectJ pointcut
	 * matching) and make available the current AspectJ JoinPoint. The call will have no effect
	 * if there are no AspectJ advisors in the advisor chain.<br/><br/>
	 *
	 * 如有必要，添加特殊顾问，以使用包含 AspectJ advisors 的代理链：具体来说，在列表开头的 {@link ExposeInvocationInterceptor}。
	 *
	 * 这将公开当前的 Spring AOP 调用（对于某些 AspectJ pointcut 匹配 是必需的），并使当前的 AspectJ JoinPoint 可用。
	 * 如果 advisor chain 中没有 AspectJ advisors，则调用将不起作用。<br/><br/>
	 *
	 * <hr>
	 *
	 * @param advisors the advisors available
	 * @return {@code true} if an {@link ExposeInvocationInterceptor} was added to the list,
	 * otherwise {@code false}
	 */
	public static boolean makeAdvisorChainAspectJCapableIfNecessary(List<Advisor> advisors) {
		// Don't add advisors to an empty list; may indicate that proxying is just not required
		if (!advisors.isEmpty()) {
			boolean foundAspectJAdvice = false;
			for (Advisor advisor : advisors) {
				// Be careful not to get the Advice without a guard, as this might eagerly
				// instantiate a non-singleton AspectJ aspect...
				if (isAspectJAdvice(advisor)) {
					foundAspectJAdvice = true;		// <aop:advisor
					break;
				}
			}
			if (foundAspectJAdvice && !advisors.contains(ExposeInvocationInterceptor.ADVISOR)) {
				advisors.add(0, ExposeInvocationInterceptor.ADVISOR);		// <== 添加到调用链头部
				return true;
			}
		}
		return false;
	}

	/**
	 * Determine whether the given Advisor contains an AspectJ advice.
	 * @param advisor the Advisor to check
	 */
	private static boolean isAspectJAdvice(Advisor advisor) {
		return (
				// @Aspect
				advisor instanceof InstantiationModelAwarePointcutAdvisor ||

				advisor.getAdvice() instanceof AbstractAspectJAdvice ||
				(advisor instanceof PointcutAdvisor &&
						((PointcutAdvisor) advisor).getPointcut() instanceof AspectJExpressionPointcut));
	}

}
