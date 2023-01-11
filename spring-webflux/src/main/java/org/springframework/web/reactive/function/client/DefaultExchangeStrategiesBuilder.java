package org.springframework.web.reactive.function.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;

/**
 * Default implementation of {@link ExchangeStrategies.Builder}.
 *
 * @author Arjen Poutsma
 * @author Brian Clozel
 * @since 5.0
 */
final class DefaultExchangeStrategiesBuilder implements ExchangeStrategies.Builder {

	final static ExchangeStrategies DEFAULT_EXCHANGE_STRATEGIES;

	static {
		DefaultExchangeStrategiesBuilder builder = new DefaultExchangeStrategiesBuilder();
		builder.defaultConfiguration();
		DEFAULT_EXCHANGE_STRATEGIES = builder.build();
	}


	private final ClientCodecConfigurer codecConfigurer;


	public DefaultExchangeStrategiesBuilder() {
		this.codecConfigurer = ClientCodecConfigurer.create();
		this.codecConfigurer.registerDefaults(false);
	}

	private DefaultExchangeStrategiesBuilder(DefaultExchangeStrategies other) {
		this.codecConfigurer = other.codecConfigurer.clone();
	}


	public void defaultConfiguration() {
		this.codecConfigurer.registerDefaults(true);
	}

	@Override
	public ExchangeStrategies.Builder codecs(Consumer<ClientCodecConfigurer> consumer) {
		consumer.accept(this.codecConfigurer);
		return this;
	}

	@Override
	public ExchangeStrategies build() {
		return new DefaultExchangeStrategies(this.codecConfigurer);
	}


	private static class DefaultExchangeStrategies implements ExchangeStrategies {

		private final ClientCodecConfigurer codecConfigurer;

		private final List<HttpMessageReader<?>> readers;

		private final List<HttpMessageWriter<?>> writers;


		public DefaultExchangeStrategies(ClientCodecConfigurer codecConfigurer) {
			this.codecConfigurer = codecConfigurer;
			this.readers = unmodifiableCopy(this.codecConfigurer.getReaders());
			this.writers = unmodifiableCopy(this.codecConfigurer.getWriters());
		}

		private static <T> List<T> unmodifiableCopy(List<? extends T> list) {
			return Collections.unmodifiableList(new ArrayList<>(list));
		}

		@Override
		public List<HttpMessageReader<?>> messageReaders() {
			return this.readers;
		}

		@Override
		public List<HttpMessageWriter<?>> messageWriters() {
			return this.writers;
		}

		@Override
		public Builder mutate() {
			return new DefaultExchangeStrategiesBuilder(this);
		}
	}

}
