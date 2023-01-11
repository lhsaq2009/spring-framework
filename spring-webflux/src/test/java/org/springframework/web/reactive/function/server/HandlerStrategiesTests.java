package org.springframework.web.reactive.function.server;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Arjen Poutsma
 */
public class HandlerStrategiesTests {

	@Test
	public void empty() {
		HandlerStrategies strategies = HandlerStrategies.empty().build();
		assertThat(strategies.messageReaders().isEmpty()).isTrue();
		assertThat(strategies.messageWriters().isEmpty()).isTrue();
		assertThat(strategies.viewResolvers().isEmpty()).isTrue();
	}

	@Test
	public void withDefaults() {
		HandlerStrategies strategies = HandlerStrategies.withDefaults();
		assertThat(strategies.messageReaders().isEmpty()).isFalse();
		assertThat(strategies.messageWriters().isEmpty()).isFalse();
		assertThat(strategies.viewResolvers().isEmpty()).isTrue();
	}

}

