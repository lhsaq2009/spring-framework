package org.springframework.aop;

import java.lang.reflect.Method;

/**
 * A specialized type of {@link MethodMatcher} that takes into account introductions
 * when matching methods. If there are no introductions on the target class,
 * a method matcher may be able to optimize matching more effectively for example.
 *
 * @author Adrian Colyer
 * @since 2.0
 */
public interface IntroductionAwareMethodMatcher extends MethodMatcher {

	/**
	 * 对指定方法，执行静态检查，是否匹配。
	 * 如果 caller，支持扩展的 IntroductionAwareMethodMatcher 接口，
	 * 则可以调用此方法而不是 2-arg matches(Method, Class) 方法。<br/><br/>
	 *
	 * <hr>
	 *
	 * Perform static checking whether the given method matches. This may be invoked
	 * instead of the 2-arg {@link #matches(java.lang.reflect.Method, Class)} method
	 * if the caller supports the extended IntroductionAwareMethodMatcher interface.
	 * @param method the candidate method
	 * @param targetClass the target class
	 * @param hasIntroductions {@code true} if the object on whose behalf we are
	 * asking is the subject on one or more introductions; {@code false} otherwise
	 * @return whether or not this method matches statically
	 */
	boolean matches(Method method, Class<?> targetClass, boolean hasIntroductions);

}
