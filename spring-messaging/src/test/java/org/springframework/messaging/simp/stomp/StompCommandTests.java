package org.springframework.messaging.simp.stomp;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.messaging.simp.SimpMessageType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 */
public class StompCommandTests {

	private static final Collection<StompCommand> destinationRequired =
			Arrays.asList(StompCommand.SEND, StompCommand.SUBSCRIBE, StompCommand.MESSAGE);

	private static final Collection<StompCommand> subscriptionIdRequired =
			Arrays.asList(StompCommand.SUBSCRIBE, StompCommand.UNSUBSCRIBE, StompCommand.MESSAGE);

	private static final Collection<StompCommand> contentLengthRequired =
			Arrays.asList(StompCommand.SEND, StompCommand.MESSAGE, StompCommand.ERROR);

	private static final Collection<StompCommand> bodyAllowed =
			Arrays.asList(StompCommand.SEND, StompCommand.MESSAGE, StompCommand.ERROR);

	private static final Map<StompCommand, SimpMessageType> messageTypes =
			new EnumMap<>(StompCommand.class);

	static {
		messageTypes.put(StompCommand.STOMP, SimpMessageType.CONNECT);
		messageTypes.put(StompCommand.CONNECT, SimpMessageType.CONNECT);
		messageTypes.put(StompCommand.DISCONNECT, SimpMessageType.DISCONNECT);
		messageTypes.put(StompCommand.SUBSCRIBE, SimpMessageType.SUBSCRIBE);
		messageTypes.put(StompCommand.UNSUBSCRIBE, SimpMessageType.UNSUBSCRIBE);
		messageTypes.put(StompCommand.SEND, SimpMessageType.MESSAGE);
		messageTypes.put(StompCommand.MESSAGE, SimpMessageType.MESSAGE);
	}


	@Test
	public void getMessageType() throws Exception {
		for (StompCommand stompCommand : StompCommand.values()) {
			SimpMessageType simp = messageTypes.get(stompCommand);
			if (simp == null) {
				simp = SimpMessageType.OTHER;
			}
			assertThat(stompCommand.getMessageType()).isSameAs(simp);
		}
	}

	@Test
	public void requiresDestination() throws Exception {
		for (StompCommand stompCommand : StompCommand.values()) {
			assertThat(stompCommand.requiresDestination()).isEqualTo(destinationRequired.contains(stompCommand));
		}
	}

	@Test
	public void requiresSubscriptionId() throws Exception {
		for (StompCommand stompCommand : StompCommand.values()) {
			assertThat(stompCommand.requiresSubscriptionId()).isEqualTo(subscriptionIdRequired.contains(stompCommand));
		}
	}

	@Test
	public void requiresContentLength() throws Exception {
		for (StompCommand stompCommand : StompCommand.values()) {
			assertThat(stompCommand.requiresContentLength()).isEqualTo(contentLengthRequired.contains(stompCommand));
		}
	}

	@Test
	public void isBodyAllowed() throws Exception {
		for (StompCommand stompCommand : StompCommand.values()) {
			assertThat(stompCommand.isBodyAllowed()).isEqualTo(bodyAllowed.contains(stompCommand));
		}
	}

}
