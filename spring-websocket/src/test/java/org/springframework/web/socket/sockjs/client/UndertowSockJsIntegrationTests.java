package org.springframework.web.socket.sockjs.client;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.UndertowTestServer;
import org.springframework.web.socket.WebSocketTestServer;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.UndertowRequestUpgradeStrategy;

/**
 * @author Brian Clozel
 */
public class UndertowSockJsIntegrationTests extends AbstractSockJsIntegrationTests {

	@Override
	protected Class<?> upgradeStrategyConfigClass() {
		return UndertowTestConfig.class;
	}

	@Override
	protected WebSocketTestServer createWebSocketTestServer() {
		return new UndertowTestServer();
	}

	@Override
	protected Transport createWebSocketTransport() {
		return new WebSocketTransport(new StandardWebSocketClient());
	}

	@Override
	protected AbstractXhrTransport createXhrTransport() {
		try {
			return new UndertowXhrTransport();
		}
		catch (IOException ex) {
			throw new IllegalStateException("Could not create UndertowXhrTransport");
		}
	}

	@Configuration
	static class UndertowTestConfig {
		@Bean
		public RequestUpgradeStrategy upgradeStrategy() {
			return new UndertowRequestUpgradeStrategy();
		}
	}
}
