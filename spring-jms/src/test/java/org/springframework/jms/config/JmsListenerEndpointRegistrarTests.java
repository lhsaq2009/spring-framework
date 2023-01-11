package org.springframework.jms.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.support.StaticListableBeanFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * @author Stephane Nicoll
 */
public class JmsListenerEndpointRegistrarTests {

	private final JmsListenerEndpointRegistrar registrar = new JmsListenerEndpointRegistrar();

	private final JmsListenerEndpointRegistry registry = new JmsListenerEndpointRegistry();

	private final JmsListenerContainerTestFactory containerFactory = new JmsListenerContainerTestFactory();


	@BeforeEach
	public void setup() {
		this.registrar.setEndpointRegistry(this.registry);
		this.registrar.setBeanFactory(new StaticListableBeanFactory());
	}


	@Test
	public void registerNullEndpoint() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				this.registrar.registerEndpoint(null, this.containerFactory));
	}

	@Test
	public void registerNullEndpointId() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				this.registrar.registerEndpoint(new SimpleJmsListenerEndpoint(), this.containerFactory));
	}

	@Test
	public void registerEmptyEndpointId() {
		SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
		endpoint.setId("");

		assertThatIllegalArgumentException().isThrownBy(() ->
				this.registrar.registerEndpoint(endpoint, this.containerFactory));
	}

	@Test
	public void registerNullContainerFactoryIsAllowed() throws Exception {
		SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
		endpoint.setId("some id");
		this.registrar.setContainerFactory(this.containerFactory);
		this.registrar.registerEndpoint(endpoint, null);
		this.registrar.afterPropertiesSet();
		assertThat(this.registry.getListenerContainer("some id")).as("Container not created").isNotNull();
		assertThat(this.registry.getListenerContainers().size()).isEqualTo(1);
		assertThat(this.registry.getListenerContainerIds().iterator().next()).isEqualTo("some id");
	}

	@Test
	public void registerNullContainerFactoryWithNoDefault() throws Exception {
		SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
		endpoint.setId("some id");
		this.registrar.registerEndpoint(endpoint, null);

		assertThatIllegalStateException().isThrownBy(() ->
				this.registrar.afterPropertiesSet())
			.withMessageContaining(endpoint.toString());
	}

	@Test
	public void registerContainerWithoutFactory() throws Exception {
		SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
		endpoint.setId("myEndpoint");
		this.registrar.setContainerFactory(this.containerFactory);
		this.registrar.registerEndpoint(endpoint);
		this.registrar.afterPropertiesSet();
		assertThat(this.registry.getListenerContainer("myEndpoint")).as("Container not created").isNotNull();
		assertThat(this.registry.getListenerContainers().size()).isEqualTo(1);
		assertThat(this.registry.getListenerContainerIds().iterator().next()).isEqualTo("myEndpoint");
	}

}
