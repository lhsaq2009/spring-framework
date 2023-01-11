package org.springframework.messaging.converter;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageHeaderAccessor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for
 * {@link org.springframework.messaging.converter.SimpleMessageConverter}.
 *
 * @author Rossen Stoyanchev
 */
public class SimpleMessageConverterTests {

	private final SimpleMessageConverter converter = new SimpleMessageConverter();


	@Test
	public void toMessageWithPayloadAndHeaders() {
		MessageHeaders headers = new MessageHeaders(Collections.<String, Object>singletonMap("foo", "bar"));
		Message<?> message = this.converter.toMessage("payload", headers);

		assertThat(message.getPayload()).isEqualTo("payload");
		assertThat(message.getHeaders().get("foo")).isEqualTo("bar");
	}

	@Test
	public void toMessageWithPayloadAndMutableHeaders() {
		MessageHeaderAccessor accessor = new MessageHeaderAccessor();
		accessor.setHeader("foo", "bar");
		accessor.setLeaveMutable(true);
		MessageHeaders headers = accessor.getMessageHeaders();

		Message<?> message = this.converter.toMessage("payload", headers);

		assertThat(message.getPayload()).isEqualTo("payload");
		assertThat(message.getHeaders()).isSameAs(headers);
		assertThat(message.getHeaders().get("foo")).isEqualTo("bar");
	}
}
