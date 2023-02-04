package org.springframework.transaction.interceptor;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.lang.Nullable;

/**
 * Advisor driven by a {@link TransactionAttributeSource}, used to include
 * a transaction advice bean for methods that are transactional.
 *
 * @author Juergen Hoeller
 * @since 2.5.5
 * @see #setAdviceBeanName
 * @see TransactionInterceptor
 * @see TransactionAttributeSourceAdvisor
 *
 *                                      Advisor                                                   MethodMatcher
 *                              PointcutAdvisor                                             StaticMethodMatcher   Pointcut
 *                      AbstractPointcutAdvisor  ( Ordered )                                      ▲               ▲
 *                                         ▲     (            Aware )                             └──────────────┐│
 *           AbstractBeanFactoryPointcutAdvisor  ( BeanFactoryAware )                          StaticMethodMatcherPointcut
 *                                         ▲                                                                      ▲
 * BeanFactoryTransactionAttributeSourceAdvisor  <tx:annotation-driven/> ─▶ pointcut ─▶ TransactionAttributeSourcePointcut
 *                                                                       ─▶ transactionAttributeSource
 *            DefaultBeanFactoryPointcutAdvisor  <aop:config>
 */
@SuppressWarnings("serial")
public class BeanFactoryTransactionAttributeSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {		// core

	/**
	 * 写入：{@link org.springframework.transaction.config.AnnotationDrivenBeanDefinitionParser.AopAutoProxyConfigurer#configureAutoProxyCreator}
	 *		AnnotationTransactionAttributeSource --> SpringTransactionAnnotationParser
	 */
	@Nullable
	private TransactionAttributeSource transactionAttributeSource;											// core

	/**
	 * abstract class TransactionAttributeSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {
	 * =>> {@link TransactionAttributeSourcePointcut#TransactionAttributeSourcePointcut}
	 */
	private final TransactionAttributeSourcePointcut pointcut = new TransactionAttributeSourcePointcut() {	// 抽象类哦！

		// 何时调用：TransactionAttributeSourcePointcut.TransactionAttributeSourceClassFilter.matches
		@Override
		@Nullable
		protected TransactionAttributeSource getTransactionAttributeSource() {
			return transactionAttributeSource;
		}
	};


	/**
	 * Set the transaction attribute source which is used to find transaction
	 * attributes. This should usually be identical to the source reference
	 * set on the transaction interceptor itself.
	 * @see TransactionInterceptor#setTransactionAttributeSource
	 */
	public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
		this.transactionAttributeSource = transactionAttributeSource;
	}

	/**
	 * Set the {@link ClassFilter} to use for this pointcut.
	 * Default is {@link ClassFilter#TRUE}.
	 */
	public void setClassFilter(ClassFilter classFilter) {		//
		this.pointcut.setClassFilter(classFilter);
	}

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

}
