package org.springframework.core.codec;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.testfixture.codec.AbstractDecoderTests;
import org.springframework.util.MimeTypeUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sebastien Deleuze
 */
class DataBufferDecoderTests extends AbstractDecoderTests<DataBufferDecoder> {

	private final byte[] fooBytes = "foo".getBytes(StandardCharsets.UTF_8);

	private final byte[] barBytes = "bar".getBytes(StandardCharsets.UTF_8);


	DataBufferDecoderTests() {
		super(new DataBufferDecoder());
	}

	@Override
	@Test
	public void canDecode() {
		assertThat(this.decoder.canDecode(ResolvableType.forClass(DataBuffer.class),
				MimeTypeUtils.TEXT_PLAIN)).isTrue();
		assertThat(this.decoder.canDecode(ResolvableType.forClass(Integer.class),
				MimeTypeUtils.TEXT_PLAIN)).isFalse();
		assertThat(this.decoder.canDecode(ResolvableType.forClass(DataBuffer.class),
				MimeTypeUtils.APPLICATION_JSON)).isTrue();
	}

	@Override
	@Test
	public void decode() {
		Flux<DataBuffer> input = Flux.just(
				this.bufferFactory.wrap(this.fooBytes),
				this.bufferFactory.wrap(this.barBytes));

		testDecodeAll(input, DataBuffer.class, step -> step
				.consumeNextWith(expectDataBuffer(this.fooBytes))
				.consumeNextWith(expectDataBuffer(this.barBytes))
				.verifyComplete());
	}

	@Override
	@Test
	public void decodeToMono() throws Exception {
		Flux<DataBuffer> input = Flux.concat(
				dataBuffer(this.fooBytes),
				dataBuffer(this.barBytes));

		byte[] expected = new byte[this.fooBytes.length + this.barBytes.length];
		System.arraycopy(this.fooBytes, 0, expected, 0, this.fooBytes.length);
		System.arraycopy(this.barBytes, 0, expected, this.fooBytes.length, this.barBytes.length);

		testDecodeToMonoAll(input, DataBuffer.class, step -> step
				.consumeNextWith(expectDataBuffer(expected))
				.verifyComplete());
	}

	private Consumer<DataBuffer> expectDataBuffer(byte[] expected) {
		return actual -> {
			byte[] actualBytes = new byte[actual.readableByteCount()];
			actual.read(actualBytes);
			assertThat(actualBytes).isEqualTo(expected);

			DataBufferUtils.release(actual);
		};
	}

}
