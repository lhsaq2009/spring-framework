package org.springframework.transaction.support;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.junit.jupiter.api.Test;

import org.springframework.context.testfixture.jndi.SimpleNamingContextBuilder;
import org.springframework.core.testfixture.io.SerializationTestUtils;
import org.springframework.transaction.jta.JtaTransactionManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author Rod Johnson
 */
public class JtaTransactionManagerSerializationTests {

	@Test
	public void serializable() throws Exception {
		UserTransaction ut1 = mock(UserTransaction.class);
		UserTransaction ut2 = mock(UserTransaction.class);
		TransactionManager tm = mock(TransactionManager.class);

		JtaTransactionManager jtam = new JtaTransactionManager();
		jtam.setUserTransaction(ut1);
		jtam.setTransactionManager(tm);
		jtam.setRollbackOnCommitFailure(true);
		jtam.afterPropertiesSet();

		SimpleNamingContextBuilder jndiEnv = SimpleNamingContextBuilder
				.emptyActivatedContextBuilder();
		jndiEnv.bind(JtaTransactionManager.DEFAULT_USER_TRANSACTION_NAME, ut2);
		JtaTransactionManager serializedJtatm = (JtaTransactionManager) SerializationTestUtils
				.serializeAndDeserialize(jtam);

		// should do client-side lookup
		assertThat(serializedJtatm.logger).as("Logger must survive serialization").isNotNull();
		assertThat(serializedJtatm
				.getUserTransaction() == ut2).as("UserTransaction looked up on client").isTrue();
		assertThat(serializedJtatm
				.getTransactionManager()).as("TransactionManager didn't survive").isNull();
		assertThat(serializedJtatm.isRollbackOnCommitFailure()).isEqualTo(true);
	}

}
