package org.springframework.jca.support;

import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link LocalConnectionFactoryBean} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public class LocalConnectionFactoryBeanTests {

	@Test
	public void testManagedConnectionFactoryIsRequired() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(
				new LocalConnectionFactoryBean()::afterPropertiesSet);
	}

	@Test
	public void testIsSingleton() throws Exception {
		LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
		assertThat(factory.isSingleton()).isTrue();
	}

	@Test
	public void testGetObjectTypeIsNullIfConnectionFactoryHasNotBeenConfigured() throws Exception {
		LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
		assertThat(factory.getObjectType()).isNull();
	}

	@Test
	public void testCreatesVanillaConnectionFactoryIfNoConnectionManagerHasBeenConfigured() throws Exception {
		final Object CONNECTION_FACTORY = new Object();
		ManagedConnectionFactory managedConnectionFactory = mock(ManagedConnectionFactory.class);
		given(managedConnectionFactory.createConnectionFactory()).willReturn(CONNECTION_FACTORY);
		LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
		factory.setManagedConnectionFactory(managedConnectionFactory);
		factory.afterPropertiesSet();
		assertThat(factory.getObject()).isEqualTo(CONNECTION_FACTORY);
	}

	@Test
	public void testCreatesManagedConnectionFactoryIfAConnectionManagerHasBeenConfigured() throws Exception {
		ManagedConnectionFactory managedConnectionFactory = mock(ManagedConnectionFactory.class);
		ConnectionManager connectionManager = mock(ConnectionManager.class);
		LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
		factory.setManagedConnectionFactory(managedConnectionFactory);
		factory.setConnectionManager(connectionManager);
		factory.afterPropertiesSet();
		verify(managedConnectionFactory).createConnectionFactory(connectionManager);
	}

}
