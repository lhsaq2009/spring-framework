package org.springframework.messaging.support;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Gary Russell
 * @since 5.0
 */
public class ErrorMessageTests {

	@Test
	public void testToString() {
		ErrorMessage em = new ErrorMessage(new RuntimeException("foo"));
		String emString = em.toString();
		assertThat(emString).doesNotContain("original");

		em = new ErrorMessage(new RuntimeException("foo"), new GenericMessage<>("bar"));
		emString = em.toString();
		assertThat(emString).contains("original");
		assertThat(emString).contains(em.getOriginalMessage().toString());
	}

}
