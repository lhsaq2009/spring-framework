package org.springframework.web.socket;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.TestInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Client and server-side WebSocket integration tests.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
class WebSocketHandshakeTests extends AbstractWebSocketIntegrationTests {

	@Override
	protected Class<?>[] getAnnotatedConfigClasses() {
		return new Class<?>[] {TestConfig.class};
	}


	@ParameterizedWebSocketTest
	void subProtocolNegotiation(WebSocketTestServer server, WebSocketClient webSocketClient, TestInfo testInfo) throws Exception {
		super.setup(server, webSocketClient, testInfo);

		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		headers.setSecWebSocketProtocol("foo");
		URI url = new URI(getWsBaseUrl() + "/ws");
		WebSocketSession session = this.webSocketClient.doHandshake(new TextWebSocketHandler(), headers, url).get();
		assertThat(session.getAcceptedProtocol()).isEqualTo("foo");
		session.close();
	}

	@ParameterizedWebSocketTest  // SPR-12727
	void unsolicitedPongWithEmptyPayload(WebSocketTestServer server, WebSocketClient webSocketClient, TestInfo testInfo) throws Exception {
		super.setup(server, webSocketClient, testInfo);

		String url = getWsBaseUrl() + "/ws";
		WebSocketSession session = this.webSocketClient.doHandshake(new AbstractWebSocketHandler() {}, url).get();

		TestWebSocketHandler serverHandler = this.wac.getBean(TestWebSocketHandler.class);
		serverHandler.setWaitMessageCount(1);

		session.sendMessage(new PongMessage());

		serverHandler.await();
		assertThat(serverHandler.getTransportError()).isNull();
		assertThat(serverHandler.getReceivedMessages().size()).isEqualTo(1);
		assertThat(serverHandler.getReceivedMessages().get(0).getClass()).isEqualTo(PongMessage.class);
	}


	@Configuration
	@EnableWebSocket
	static class TestConfig implements WebSocketConfigurer {

		@Autowired
		private DefaultHandshakeHandler handshakeHandler;  // can't rely on classpath for server detection

		@Override
		public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
			this.handshakeHandler.setSupportedProtocols("foo", "bar", "baz");
			registry.addHandler(handler(), "/ws").setHandshakeHandler(this.handshakeHandler);
		}

		@Bean
		public TestWebSocketHandler handler() {
			return new TestWebSocketHandler();
		}

	}

	@SuppressWarnings("rawtypes")
	private static class TestWebSocketHandler extends AbstractWebSocketHandler {

		private List<WebSocketMessage> receivedMessages = new ArrayList<>();

		private int waitMessageCount;

		private final CountDownLatch latch = new CountDownLatch(1);

		private Throwable transportError;

		public void setWaitMessageCount(int waitMessageCount) {
			this.waitMessageCount = waitMessageCount;
		}

		public List<WebSocketMessage> getReceivedMessages() {
			return this.receivedMessages;
		}

		public Throwable getTransportError() {
			return this.transportError;
		}

		@Override
		public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
			this.receivedMessages.add(message);
			if (this.receivedMessages.size() >= this.waitMessageCount) {
				this.latch.countDown();
			}
		}

		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
			this.transportError = exception;
			this.latch.countDown();
		}

		public void await() throws InterruptedException {
			this.latch.await(5, TimeUnit.SECONDS);
		}
	}

}
