package org.springframework.jms.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * @author Stephane Nicoll
 */
public class JmsListenerEndpointRegistryTests {

	private final JmsListenerEndpointRegistry registry = new JmsListenerEndpointRegistry();

	private final JmsListenerContainerTestFactory containerFactory = new JmsListenerContainerTestFactory();


	@Test
	public void createWithNullEndpoint() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				registry.registerListenerContainer(null, containerFactory));
	}

	@Test
	public void createWithNullEndpointId() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				registry.registerListenerContainer(new SimpleJmsListenerEndpoint(), containerFactory));
	}

	@Test
	public void createWithNullContainerFactory() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				registry.registerListenerContainer(createEndpoint("foo", "myDestination"), null));
	}

	@Test
	public void createWithDuplicateEndpointId() {
		registry.registerListenerContainer(createEndpoint("test", "queue"), containerFactory);

		assertThatIllegalStateException().isThrownBy(() ->
				registry.registerListenerContainer(createEndpoint("test", "queue"), containerFactory));
	}


	private SimpleJmsListenerEndpoint createEndpoint(String id, String destinationName) {
		SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
		endpoint.setId(id);
		endpoint.setDestination(destinationName);
		return endpoint;
	}

}
