package org.springframework.messaging.simp;

import org.junit.jupiter.api.Test;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for SimpMessageTypeMessageCondition.
 *
 * @author Rossen Stoyanchev
 */
public class SimpMessageTypeMessageConditionTests {

	@Test
	public void combine() {
		SimpMessageType messageType = SimpMessageType.MESSAGE;
		SimpMessageType subscribeType = SimpMessageType.SUBSCRIBE;

		SimpMessageType actual = condition(messageType).combine(condition(subscribeType)).getMessageType();
		assertThat(actual).isEqualTo(subscribeType);

		actual = condition(messageType).combine(condition(messageType)).getMessageType();
		assertThat(actual).isEqualTo(messageType);

		actual = condition(subscribeType).combine(condition(subscribeType)).getMessageType();
		assertThat(actual).isEqualTo(subscribeType);
	}

	@Test
	public void getMatchingCondition() {
		Message<?> message = message(SimpMessageType.MESSAGE);
		SimpMessageTypeMessageCondition condition = condition(SimpMessageType.MESSAGE);
		SimpMessageTypeMessageCondition actual = condition.getMatchingCondition(message);

		assertThat(actual).isNotNull();
		assertThat(actual.getMessageType()).isEqualTo(SimpMessageType.MESSAGE);
	}

	@Test
	public void getMatchingConditionNoMessageType() {
		Message<?> message = message(null);
		SimpMessageTypeMessageCondition condition = condition(SimpMessageType.MESSAGE);

		assertThat(condition.getMatchingCondition(message)).isNull();
	}

	@Test
	public void compareTo() {
		Message<byte[]> message = message(null);
		assertThat(condition(SimpMessageType.MESSAGE).compareTo(condition(SimpMessageType.MESSAGE), message)).isEqualTo(0);
		assertThat(condition(SimpMessageType.MESSAGE).compareTo(condition(SimpMessageType.SUBSCRIBE), message)).isEqualTo(0);
	}

	private Message<byte[]> message(SimpMessageType messageType) {
		MessageBuilder<byte[]> builder = MessageBuilder.withPayload(new byte[0]);
		if (messageType != null) {
			builder.setHeader(SimpMessageHeaderAccessor.MESSAGE_TYPE_HEADER, messageType);
		}
		return builder.build();
	}

	private SimpMessageTypeMessageCondition condition(SimpMessageType messageType) {
		return new SimpMessageTypeMessageCondition(messageType);
	}

}
