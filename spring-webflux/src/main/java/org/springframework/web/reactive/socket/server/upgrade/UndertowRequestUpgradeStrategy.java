package org.springframework.web.reactive.socket.server.upgrade;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import io.undertow.server.HttpServerExchange;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.WebSocketProtocolHandshakeHandler;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.protocol.Handshake;
import io.undertow.websockets.core.protocol.version13.Hybi13Handshake;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import reactor.core.publisher.Mono;

import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.AbstractServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.adapter.UndertowWebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.adapter.UndertowWebSocketSession;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;
import org.springframework.web.server.ServerWebExchange;

/**
 * A {@link RequestUpgradeStrategy} for use with Undertow.
 *
 * @author Violeta Georgieva
 * @author Rossen Stoyanchev
 * @author Brian Clozel
 * @since 5.0
 */
public class UndertowRequestUpgradeStrategy implements RequestUpgradeStrategy {

	@Override
	public Mono<Void> upgrade(ServerWebExchange exchange, WebSocketHandler handler,
			@Nullable String subProtocol, Supplier<HandshakeInfo> handshakeInfoFactory) {

		HttpServerExchange httpExchange = getNativeRequest(exchange.getRequest());

		Set<String> protocols = (subProtocol != null ? Collections.singleton(subProtocol) : Collections.emptySet());
		Hybi13Handshake handshake = new Hybi13Handshake(protocols, false);
		List<Handshake> handshakes = Collections.singletonList(handshake);

		HandshakeInfo handshakeInfo = handshakeInfoFactory.get();
		DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();

		// Trigger WebFlux preCommit actions and upgrade
		return exchange.getResponse().setComplete()
				.then(Mono.fromCallable(() -> {
					DefaultCallback callback = new DefaultCallback(handshakeInfo, handler, bufferFactory);
					new WebSocketProtocolHandshakeHandler(handshakes, callback).handleRequest(httpExchange);
					return null;
				}));
	}

	private static HttpServerExchange getNativeRequest(ServerHttpRequest request) {
		if (request instanceof AbstractServerHttpRequest) {
			return ((AbstractServerHttpRequest) request).getNativeRequest();
		}
		else if (request instanceof ServerHttpRequestDecorator) {
			return getNativeRequest(((ServerHttpRequestDecorator) request).getDelegate());
		}
		else {
			throw new IllegalArgumentException(
					"Couldn't find HttpServerExchange in " + request.getClass().getName());
		}
	}


	private class DefaultCallback implements WebSocketConnectionCallback {

		private final HandshakeInfo handshakeInfo;

		private final WebSocketHandler handler;

		private final DataBufferFactory bufferFactory;

		public DefaultCallback(HandshakeInfo handshakeInfo, WebSocketHandler handler, DataBufferFactory bufferFactory) {
			this.handshakeInfo = handshakeInfo;
			this.handler = handler;
			this.bufferFactory = bufferFactory;
		}

		@Override
		public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
			UndertowWebSocketSession session = createSession(channel);
			UndertowWebSocketHandlerAdapter adapter = new UndertowWebSocketHandlerAdapter(session);

			channel.getReceiveSetter().set(adapter);
			channel.resumeReceives();

			this.handler.handle(session)
					.checkpoint(exchange.getRequestURI() + " [UndertowRequestUpgradeStrategy]")
					.subscribe(session);
		}

		private UndertowWebSocketSession createSession(WebSocketChannel channel) {
			return new UndertowWebSocketSession(channel, this.handshakeInfo, this.bufferFactory);
		}
	}

}
