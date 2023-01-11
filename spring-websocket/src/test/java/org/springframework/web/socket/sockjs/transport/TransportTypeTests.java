package org.springframework.web.socket.sockjs.transport;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rossen Stoyanchev
 */
public class TransportTypeTests {

	@Test
	public void testFromValue() {
		assertThat(TransportType.fromValue("websocket")).isEqualTo(TransportType.WEBSOCKET);
		assertThat(TransportType.fromValue("xhr")).isEqualTo(TransportType.XHR);
		assertThat(TransportType.fromValue("xhr_send")).isEqualTo(TransportType.XHR_SEND);
		assertThat(TransportType.fromValue("xhr_streaming")).isEqualTo(TransportType.XHR_STREAMING);
		assertThat(TransportType.fromValue("eventsource")).isEqualTo(TransportType.EVENT_SOURCE);
		assertThat(TransportType.fromValue("htmlfile")).isEqualTo(TransportType.HTML_FILE);
	}

}
