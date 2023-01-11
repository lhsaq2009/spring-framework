package org.springframework.messaging.simp.stomp;

import org.junit.jupiter.api.Test;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test fixture for {@link StompEncoder}.
 *
 * @author Andy Wilkinson
 * @author Stephane Maldini
 */
public class StompEncoderTests {

	private final StompEncoder encoder = new StompEncoder();


	@Test
	public void encodeFrameWithNoHeadersAndNoBody() {
		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		Message<byte[]> frame = MessageBuilder.createMessage(new byte[0], headers.getMessageHeaders());

		assertThat(new String(encoder.encode(frame))).isEqualTo("DISCONNECT\n\n\0");
	}

	@Test
	public void encodeFrameWithHeaders() {
		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.CONNECT);
		headers.setAcceptVersion("1.2");
		headers.setHost("github.org");
		Message<byte[]> frame = MessageBuilder.createMessage(new byte[0], headers.getMessageHeaders());
		String frameString = new String(encoder.encode(frame));

		assertThat("CONNECT\naccept-version:1.2\nhost:github.org\n\n\0".equals(frameString) ||
				"CONNECT\nhost:github.org\naccept-version:1.2\n\n\0".equals(frameString)).isTrue();
	}

	@Test
	public void encodeFrameWithHeadersThatShouldBeEscaped() {
		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		headers.addNativeHeader("a:\r\n\\b",  "alpha:bravo\r\n\\");
		Message<byte[]> frame = MessageBuilder.createMessage(new byte[0], headers.getMessageHeaders());

		assertThat(new String(encoder.encode(frame))).isEqualTo("DISCONNECT\na\\c\\r\\n\\\\b:alpha\\cbravo\\r\\n\\\\\n\n\0");
	}

	@Test
	public void encodeFrameWithHeadersBody() {
		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
		headers.addNativeHeader("a", "alpha");
		Message<byte[]> frame = MessageBuilder.createMessage(
				"Message body".getBytes(), headers.getMessageHeaders());

		assertThat(new String(encoder.encode(frame))).isEqualTo("SEND\na:alpha\ncontent-length:12\n\nMessage body\0");
	}

	@Test
	public void encodeFrameWithContentLengthPresent() {
		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
		headers.setContentLength(12);
		Message<byte[]> frame = MessageBuilder.createMessage(
				"Message body".getBytes(), headers.getMessageHeaders());

		assertThat(new String(encoder.encode(frame))).isEqualTo("SEND\ncontent-length:12\n\nMessage body\0");
	}

}
