package org.springframework.core.codec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import org.springframework.core.ResolvableType;
import org.springframework.core.testfixture.codec.AbstractEncoderTests;
import org.springframework.util.MimeTypeUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sebastien Deleuze
 */
class ByteBufferEncoderTests extends AbstractEncoderTests<ByteBufferEncoder> {

	private final byte[] fooBytes = "foo".getBytes(StandardCharsets.UTF_8);

	private final byte[] barBytes = "bar".getBytes(StandardCharsets.UTF_8);

	ByteBufferEncoderTests() {
		super(new ByteBufferEncoder());
	}

	@Override
	@Test
	public void canEncode() {
		assertThat(this.encoder.canEncode(ResolvableType.forClass(ByteBuffer.class),
				MimeTypeUtils.TEXT_PLAIN)).isTrue();
		assertThat(this.encoder.canEncode(ResolvableType.forClass(Integer.class),
				MimeTypeUtils.TEXT_PLAIN)).isFalse();
		assertThat(this.encoder.canEncode(ResolvableType.forClass(ByteBuffer.class),
				MimeTypeUtils.APPLICATION_JSON)).isTrue();

		// SPR-15464
		assertThat(this.encoder.canEncode(ResolvableType.NONE, null)).isFalse();
	}

	@Override
	@Test
	public void encode() {
		Flux<ByteBuffer> input = Flux.just(this.fooBytes, this.barBytes)
				.map(ByteBuffer::wrap);

		testEncodeAll(input, ByteBuffer.class, step -> step
				.consumeNextWith(expectBytes(this.fooBytes))
				.consumeNextWith(expectBytes(this.barBytes))
				.verifyComplete());
	}

}
