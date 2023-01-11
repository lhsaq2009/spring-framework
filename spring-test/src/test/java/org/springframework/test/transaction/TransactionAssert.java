package org.springframework.test.transaction;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Collection of assertions for tests involving transactions. Intended for
 * internal use within the Spring testing suite.
 *
 * @author Sam Brannen
 * @author Phillip Webb
 * @since 5.2
 */
public class TransactionAssert {

	private static final TransactionAssert instance = new TransactionAssert();

	public TransactionAssert isActive() {
		return isInTransaction(true);
	}

	public TransactionAssert isNotActive() {
		return isInTransaction(false);

	}

	public TransactionAssert isInTransaction(boolean expected) {
		assertThat(TransactionSynchronizationManager.isActualTransactionActive())
				.as("active transaction")
				.isEqualTo(expected);
		return this;
	}

	public static TransactionAssert assertThatTransaction() {
		return instance;
	}

}
