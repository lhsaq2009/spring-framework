package org.springframework.transaction.interceptor;

import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionDefinition;

/**
 * This interface adds a {@code rollbackOn} specification to {@link TransactionDefinition}.
 * As custom {@code rollbackOn} is only possible with AOP, it resides in the AOP-related
 * transaction subpackage.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 16.03.2003
 * @see DefaultTransactionAttribute
 * @see RuleBasedTransactionAttribute
 */
public interface TransactionAttribute extends TransactionDefinition {

	/**
	 * Return a qualifier value associated with this transaction attribute.
	 * <p>This may be used for choosing a corresponding transaction manager
	 * to process this specific transaction.
	 * @since 3.0
	 */
	@Nullable
	String getQualifier();		// TODO：分析 tx 时，没赋值的地方呀，什么情况会用到呢，2023-01-12

	/**
	 * Should we roll back on the given exception?
	 * @param ex the exception to evaluate
	 * @return whether to perform a rollback or not
	 */
	boolean rollbackOn(Throwable ex);

}
