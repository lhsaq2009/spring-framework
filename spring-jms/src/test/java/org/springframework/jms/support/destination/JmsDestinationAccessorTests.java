package org.springframework.jms.support.destination;

import javax.jms.ConnectionFactory;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;

/**
 * @author Rick Evans
 * @author Chris Beams
 */
public class JmsDestinationAccessorTests {

	@Test
	public void testChokesIfDestinationResolverIsetToNullExplicitly() throws Exception {
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);

		JmsDestinationAccessor accessor = new StubJmsDestinationAccessor();
		accessor.setConnectionFactory(connectionFactory);
		assertThatIllegalArgumentException().isThrownBy(() ->
				accessor.setDestinationResolver(null));
	}

	@Test
	public void testSessionTransactedModeReallyDoesDefaultToFalse() throws Exception {
		JmsDestinationAccessor accessor = new StubJmsDestinationAccessor();
		assertThat(accessor.isPubSubDomain()).as("The [pubSubDomain] property of JmsDestinationAccessor must default to " +
				"false (i.e. Queues are used by default). Change this test (and the " +
				"attendant Javadoc) if you have changed the default.").isFalse();
	}


	private static class StubJmsDestinationAccessor extends JmsDestinationAccessor {

	}

}
