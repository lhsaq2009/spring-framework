package org.springframework.web.socket.handler;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test fixture for {@link WebSocketHandlerDecorator}.
 *
 * @author Rossen Stoyanchev
 */
public class WebSocketHandlerDecoratorTests {

	@Test
	public void getLastHandler() {
		AbstractWebSocketHandler h1 = new AbstractWebSocketHandler() {
		};
		WebSocketHandlerDecorator h2 = new WebSocketHandlerDecorator(h1);
		WebSocketHandlerDecorator h3 = new WebSocketHandlerDecorator(h2);

		assertThat(h3.getLastHandler()).isSameAs(h1);
	}

}
