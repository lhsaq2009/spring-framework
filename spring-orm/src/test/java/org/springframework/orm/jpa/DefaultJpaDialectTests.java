package org.springframework.orm.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;

import org.junit.jupiter.api.Test;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Costin Leau
 * @author Phillip Webb
 */
public class DefaultJpaDialectTests {

	private JpaDialect dialect = new DefaultJpaDialect();

	@Test
	public void testDefaultTransactionDefinition() throws Exception {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() ->
				dialect.beginTransaction(null, definition));
	}

	@Test
	public void testDefaultBeginTransaction() throws Exception {
		TransactionDefinition definition = new DefaultTransactionDefinition();
		EntityManager entityManager = mock(EntityManager.class);
		EntityTransaction entityTx = mock(EntityTransaction.class);

		given(entityManager.getTransaction()).willReturn(entityTx);

		dialect.beginTransaction(entityManager, definition);
	}

	@Test
	public void testTranslateException() {
		OptimisticLockException ex = new OptimisticLockException();
		assertThat(dialect.translateExceptionIfPossible(ex).getCause()).isEqualTo(EntityManagerFactoryUtils.convertJpaAccessExceptionIfPossible(ex).getCause());
	}
}
