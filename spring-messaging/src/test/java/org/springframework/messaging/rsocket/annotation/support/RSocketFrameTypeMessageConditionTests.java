package org.springframework.messaging.rsocket.annotation.support;

import java.util.Arrays;

import io.rsocket.frame.FrameType;
import org.junit.jupiter.api.Test;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.messaging.rsocket.annotation.support.RSocketFrameTypeMessageCondition.CONNECT_CONDITION;
import static org.springframework.messaging.rsocket.annotation.support.RSocketFrameTypeMessageCondition.EMPTY_CONDITION;

/**
 * Unit tests for {@link RSocketFrameTypeMessageCondition}.
 * @author Rossen Stoyanchev
 */
public class RSocketFrameTypeMessageConditionTests {

	private static final RSocketFrameTypeMessageCondition FNF_RR_CONDITION =
			new RSocketFrameTypeMessageCondition(FrameType.REQUEST_FNF, FrameType.REQUEST_RESPONSE);


	@Test
	public void getMatchingCondition() {
		Message<?> message = message(FrameType.REQUEST_RESPONSE);
		RSocketFrameTypeMessageCondition actual = FNF_RR_CONDITION.getMatchingCondition(message);

		assertThat(actual).isNotNull();
		assertThat(actual.getFrameTypes()).hasSize(1).containsOnly(FrameType.REQUEST_RESPONSE);
	}

	@Test
	public void getMatchingConditionEmpty() {
		Message<?> message = message(FrameType.REQUEST_RESPONSE);
		RSocketFrameTypeMessageCondition actual = EMPTY_CONDITION.getMatchingCondition(message);

		assertThat(actual).isNull();
	}

	@Test
	public void combine() {

		assertThat(EMPTY_CONDITION.combine(CONNECT_CONDITION).getFrameTypes())
				.containsExactly(FrameType.SETUP, FrameType.METADATA_PUSH);

		assertThat(EMPTY_CONDITION.combine(new RSocketFrameTypeMessageCondition(FrameType.REQUEST_FNF)).getFrameTypes())
				.containsExactly(FrameType.REQUEST_FNF);
	}

	@Test
	public void compareTo() {
		Message<byte[]> message = message(null);
		assertThat(condition(FrameType.SETUP).compareTo(condition(FrameType.SETUP), message)).isEqualTo(0);
		assertThat(condition(FrameType.SETUP).compareTo(condition(FrameType.METADATA_PUSH), message)).isEqualTo(0);
	}

	private Message<byte[]> message(@Nullable FrameType frameType) {
		MessageBuilder<byte[]> builder = MessageBuilder.withPayload(new byte[0]);
		if (frameType != null) {
			builder.setHeader(RSocketFrameTypeMessageCondition.FRAME_TYPE_HEADER, frameType);
		}
		return builder.build();
	}

	private RSocketFrameTypeMessageCondition condition(FrameType... frameType) {
		return new RSocketFrameTypeMessageCondition(Arrays.asList(frameType));
	}

}
