package org.springframework.jms.listener.adapter;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.junit.jupiter.api.Test;

import org.springframework.jms.support.destination.DestinationResolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Stephane Nicoll
 */
public class JmsResponseTests {

	@Test
	public void destinationDoesNotUseDestinationResolver() throws JMSException {
		Destination destination = mock(Destination.class);
		Destination actual = JmsResponse.forDestination("foo", destination).resolveDestination(null, null);
		assertThat(actual).isSameAs(destination);
	}

	@Test
	public void resolveDestinationForQueue() throws JMSException {
		Session session = mock(Session.class);
		DestinationResolver destinationResolver = mock(DestinationResolver.class);
		Destination destination = mock(Destination.class);

		given(destinationResolver.resolveDestinationName(session, "myQueue", false)).willReturn(destination);
		JmsResponse<String> jmsResponse = JmsResponse.forQueue("foo", "myQueue");
		Destination actual = jmsResponse.resolveDestination(destinationResolver, session);
		assertThat(actual).isSameAs(destination);
	}

	@Test
	public void createWithNulResponse() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				JmsResponse.forQueue(null, "myQueue"));
	}

	@Test
	public void createWithNullQueueName() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				JmsResponse.forQueue("foo", null));
	}

	@Test
	public void createWithNullTopicName() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				JmsResponse.forTopic("foo", null));
	}

	@Test
	public void createWithNulDestination() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				JmsResponse.forDestination("foo", null));
	}

}
