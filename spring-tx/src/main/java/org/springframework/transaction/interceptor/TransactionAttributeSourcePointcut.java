package org.springframework.transaction.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionManager;
import org.springframework.util.ObjectUtils;

/**
 * Abstract class that implements a Pointcut that matches if the underlying
 * {@link TransactionAttributeSource} has an attribute for a given method.
 *
 * @author Juergen Hoeller
 * @since 2.5.5
 */
@SuppressWarnings("serial")
abstract class TransactionAttributeSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {

	protected TransactionAttributeSourcePointcut() {
		// 父类：StaticMethodMatcherPointcut.classFilter
		setClassFilter(new TransactionAttributeSourceClassFilter());	// 检测类
	}


	@Override
	public boolean matches(Method method, Class<?> targetClass) {		// 检测方法
		// =>> 实质：BeanFactoryTransactionAttributeSourceAdvisor.transactionAttributeSource
		/**
		 * {@link org.springframework.transaction.annotation.AnnotationTransactionAttributeSource}
		 * =>> 配置 {@link org.springframework.transaction.annotation.SpringTransactionAnnotationParser} 解析 @Transactional
		 */
		TransactionAttributeSource tas = getTransactionAttributeSource();
		return (tas == null || tas.getTransactionAttribute(method, targetClass) != null);	// =>> method
	}

	@Override
	public boolean equals(@Nullable Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TransactionAttributeSourcePointcut)) {
			return false;
		}
		TransactionAttributeSourcePointcut otherPc = (TransactionAttributeSourcePointcut) other;
		return ObjectUtils.nullSafeEquals(getTransactionAttributeSource(), otherPc.getTransactionAttributeSource());
	}

	@Override
	public int hashCode() {
		return TransactionAttributeSourcePointcut.class.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getName() + ": " + getTransactionAttributeSource();
	}


	/**
	 * Obtain the underlying TransactionAttributeSource (may be {@code null}).
	 * To be implemented by subclasses.
	 */
	@Nullable
	protected abstract TransactionAttributeSource getTransactionAttributeSource();


	/**
	 * {@link ClassFilter} that delegates to {@link TransactionAttributeSource#isCandidateClass}
	 * for filtering classes whose methods are not worth searching to begin with.
	 */
	private class TransactionAttributeSourceClassFilter implements ClassFilter {		// core

		@Override  //
		public boolean matches(Class<?> clazz) {
			if (TransactionalProxy.class.isAssignableFrom(clazz) ||
					TransactionManager.class.isAssignableFrom(clazz) ||
					PersistenceExceptionTranslator.class.isAssignableFrom(clazz)) {
				return false;
			}
			/**
			 * =>> BeanFactoryTransactionAttributeSourceAdvisor
			 * 	   匿名内部类：new TransactionAttributeSourcePointcut()
			 * 	   =>> tas = {@link BeanFactoryTransactionAttributeSourceAdvisor#transactionAttributeSource}
			 */
			TransactionAttributeSource tas = getTransactionAttributeSource();
			/**
			 * CASE 1. tas = {AnnotationTransactionAttributeSource@4555}
			 * 		   =>> {@link org.springframework.transaction.annotation.AnnotationTransactionAttributeSource#isCandidateClass}
			 */
			return (tas == null || tas.isCandidateClass(clazz));	// =>>
		}
	}

}
