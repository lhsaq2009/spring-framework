package org.springframework.web.socket.handler;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketHttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Unit tests for WebSocketHttpHeaders.
 *
 * @author Rossen Stoyanchev
 */
public class WebSocketHttpHeadersTests {

	private WebSocketHttpHeaders headers;

	@BeforeEach
	public void setUp() {
		headers = new WebSocketHttpHeaders();
	}

	@Test
	public void parseWebSocketExtensions() {
		List<String> extensions = new ArrayList<>();
		extensions.add("x-foo-extension, x-bar-extension");
		extensions.add("x-test-extension");
		this.headers.put(WebSocketHttpHeaders.SEC_WEBSOCKET_EXTENSIONS, extensions);

		List<WebSocketExtension> parsedExtensions = this.headers.getSecWebSocketExtensions();
		assertThat(parsedExtensions).hasSize(3);
	}

}
