package org.springframework.web.socket.server.standard;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test fixture for {@link ServerEndpointRegistration}.
 *
 * @author Rossen Stoyanchev
 */
public class ServerEndpointRegistrationTests {


	@Test
	public void endpointPerConnection() throws Exception {

		@SuppressWarnings("resource")
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

		ServerEndpointRegistration registration = new ServerEndpointRegistration("/path", EchoEndpoint.class);
		registration.setBeanFactory(context.getBeanFactory());

		EchoEndpoint endpoint = registration.getConfigurator().getEndpointInstance(EchoEndpoint.class);

		assertThat(endpoint).isNotNull();
	}

	@Test
	public void endpointSingleton() throws Exception {

		EchoEndpoint endpoint = new EchoEndpoint(new EchoService());
		ServerEndpointRegistration registration = new ServerEndpointRegistration("/path", endpoint);

		EchoEndpoint actual = registration.getConfigurator().getEndpointInstance(EchoEndpoint.class);

		assertThat(actual).isSameAs(endpoint);
	}


	@Configuration
	static class Config {

		@Bean
		public EchoService echoService() {
			return new EchoService();
		}
	}

	private static class EchoEndpoint extends Endpoint {

		@SuppressWarnings("unused")
		private final EchoService service;

		@Autowired
		public EchoEndpoint(EchoService service) {
			this.service = service;
		}

		@Override
		public void onOpen(Session session, EndpointConfig config) {
		}
	}

	private static class EchoService {	}

}
