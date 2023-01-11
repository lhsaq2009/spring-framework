package org.springframework.web.reactive.function.client;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Arjen Poutsma
 */
public class ExchangeStrategiesTests {

	@Test
	public void empty() {
		ExchangeStrategies strategies = ExchangeStrategies.empty().build();
		assertThat(strategies.messageReaders().isEmpty()).isTrue();
		assertThat(strategies.messageWriters().isEmpty()).isTrue();
	}

	@Test
	public void withDefaults() {
		ExchangeStrategies strategies = ExchangeStrategies.withDefaults();
		assertThat(strategies.messageReaders().isEmpty()).isFalse();
		assertThat(strategies.messageWriters().isEmpty()).isFalse();
	}

	@Test
	@SuppressWarnings("deprecation")
	public void mutate() {
		ExchangeStrategies strategies = ExchangeStrategies.empty().build();
		assertThat(strategies.messageReaders().isEmpty()).isTrue();
		assertThat(strategies.messageWriters().isEmpty()).isTrue();

		ExchangeStrategies mutated = strategies.mutate().codecs(codecs -> codecs.registerDefaults(true)).build();
		assertThat(mutated.messageReaders().isEmpty()).isFalse();
		assertThat(mutated.messageWriters().isEmpty()).isFalse();
	}

}
