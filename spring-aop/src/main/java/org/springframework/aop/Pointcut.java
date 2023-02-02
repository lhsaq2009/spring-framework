package org.springframework.aop;

/**
 * Core Spring pointcut abstraction.
 *
 * <p>A pointcut is composed of a {@link ClassFilter} and a {@link MethodMatcher}.
 * Both these basic terms and a Pointcut itself can be combined to build up combinations
 * (e.g. through {@link org.springframework.aop.support.ComposablePointcut}).
 *
 * @author Rod Johnson
 * @see ClassFilter
 * @see MethodMatcher
 * @see org.springframework.aop.support.Pointcuts
 * @see org.springframework.aop.support.ClassFilters
 * @see org.springframework.aop.support.MethodMatchers
 */
public interface Pointcut {

	/**
	 * 返回此 pointcut 的 ClassFilter ；	Return the ClassFilter for this pointcut.<br/><br/>
	 *
	 * @return the ClassFilter (never {@code null})
	 */
	ClassFilter getClassFilter();

	/**
	 * 返回此 pointcut 的 MethodMatcher；		Return the MethodMatcher for this pointcut. <br/><br/>
	 *
	 * @return the MethodMatcher (never {@code null})
	 */
	MethodMatcher getMethodMatcher();

	/**
	 * 始终匹配「规范的 Pointcut 实例」
	 * Canonical Pointcut instance that always matches.
	 */
	Pointcut TRUE = TruePointcut.INSTANCE;

}
