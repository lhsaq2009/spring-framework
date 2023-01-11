package org.springframework.transaction;

/**
 * Representation of an ongoing reactive transaction.
 * This is currently a marker interface extending {@link TransactionExecution}
 * but may acquire further methods in a future revision.
 *
 * <p>Transactional code can use this to retrieve status information,
 * and to programmatically request a rollback (instead of throwing
 * an exception that causes an implicit rollback).
 *
 * @author Mark Paluch
 * @author Juergen Hoeller
 * @since 5.2
 * @see #setRollbackOnly()
 * @see ReactiveTransactionManager#getReactiveTransaction
 */
public interface ReactiveTransaction extends TransactionExecution {

}
