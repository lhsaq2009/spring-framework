package org.springframework.jms.support;

import javax.jms.Session;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for the {@link JmsAccessor} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public class JmsAccessorTests {

	@Test
	public void testChokesIfConnectionFactoryIsNotSupplied() throws Exception {
		JmsAccessor accessor = new StubJmsAccessor();
		assertThatIllegalArgumentException().isThrownBy(
				accessor::afterPropertiesSet);
	}

	@Test
	public void testSessionTransactedModeReallyDoesDefaultToFalse() throws Exception {
		JmsAccessor accessor = new StubJmsAccessor();
		assertThat(accessor.isSessionTransacted()).as("The [sessionTransacted] property of JmsAccessor must default to " +
				"false. Change this test (and the attendant Javadoc) if you have " +
				"changed the default.").isFalse();
	}

	@Test
	public void testAcknowledgeModeReallyDoesDefaultToAutoAcknowledge() throws Exception {
		JmsAccessor accessor = new StubJmsAccessor();
		assertThat(accessor.getSessionAcknowledgeMode()).as("The [sessionAcknowledgeMode] property of JmsAccessor must default to " +
				"[Session.AUTO_ACKNOWLEDGE]. Change this test (and the attendant " +
				"Javadoc) if you have changed the default.").isEqualTo(Session.AUTO_ACKNOWLEDGE);
	}

	@Test
	public void testSetAcknowledgeModeNameChokesIfBadAckModeIsSupplied() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new StubJmsAccessor().setSessionAcknowledgeModeName("Tally ho chaps!"));
	}


	/**
	 * Crummy, stub, do-nothing subclass of the JmsAccessor class for use in testing.
	 */
	private static final class StubJmsAccessor extends JmsAccessor {
	}

}
