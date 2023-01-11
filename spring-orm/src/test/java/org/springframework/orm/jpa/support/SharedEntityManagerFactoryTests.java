package org.springframework.orm.jpa.support;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.Test;

import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.EntityManagerProxy;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Phillip Webb
 */
public class SharedEntityManagerFactoryTests {

	@Test
	public void testValidUsage() {
		Object o = new Object();

		EntityManager mockEm = mock(EntityManager.class);
		given(mockEm.isOpen()).willReturn(true);

		EntityManagerFactory mockEmf = mock(EntityManagerFactory.class);
		given(mockEmf.createEntityManager()).willReturn(mockEm);

		SharedEntityManagerBean proxyFactoryBean = new SharedEntityManagerBean();
		proxyFactoryBean.setEntityManagerFactory(mockEmf);
		proxyFactoryBean.afterPropertiesSet();

		assertThat(EntityManager.class.isAssignableFrom(proxyFactoryBean.getObjectType())).isTrue();
		assertThat(proxyFactoryBean.isSingleton()).isTrue();

		EntityManager proxy = proxyFactoryBean.getObject();
		assertThat(proxyFactoryBean.getObject()).isSameAs(proxy);
		assertThat(proxy.contains(o)).isFalse();

		boolean condition = proxy instanceof EntityManagerProxy;
		assertThat(condition).isTrue();
		EntityManagerProxy emProxy = (EntityManagerProxy) proxy;
		assertThatIllegalStateException().as("outside of transaction").isThrownBy(
				emProxy::getTargetEntityManager);

		TransactionSynchronizationManager.bindResource(mockEmf, new EntityManagerHolder(mockEm));
		try {
			assertThat(emProxy.getTargetEntityManager()).isSameAs(mockEm);
		}
		finally {
			TransactionSynchronizationManager.unbindResource(mockEmf);
		}

		assertThat(TransactionSynchronizationManager.getResourceMap().isEmpty()).isTrue();
		verify(mockEm).contains(o);
		verify(mockEm).close();
	}

}
