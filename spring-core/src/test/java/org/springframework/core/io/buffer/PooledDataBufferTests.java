package org.springframework.core.io.buffer;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * @author Arjen Poutsma
 * @author Sam Brannen
 */
class PooledDataBufferTests {

	@Nested
	class UnpooledByteBufAllocatorWithPreferDirectTrueTests implements PooledDataBufferTestingTrait {

		@Override
		public DataBufferFactory createDataBufferFactory() {
			return new NettyDataBufferFactory(new UnpooledByteBufAllocator(true));
		}
	}

	@Nested
	class UnpooledByteBufAllocatorWithPreferDirectFalseTests implements PooledDataBufferTestingTrait {

		@Override
		public DataBufferFactory createDataBufferFactory() {
			return new NettyDataBufferFactory(new UnpooledByteBufAllocator(true));
		}
	}

	@Nested
	class PooledByteBufAllocatorWithPreferDirectTrueTests implements PooledDataBufferTestingTrait {

		@Override
		public DataBufferFactory createDataBufferFactory() {
			return new NettyDataBufferFactory(new PooledByteBufAllocator(true));
		}
	}

	@Nested
	class PooledByteBufAllocatorWithPreferDirectFalseTests implements PooledDataBufferTestingTrait {

		@Override
		public DataBufferFactory createDataBufferFactory() {
			return new NettyDataBufferFactory(new PooledByteBufAllocator(true));
		}
	}

	interface PooledDataBufferTestingTrait {

		DataBufferFactory createDataBufferFactory();

		default PooledDataBuffer createDataBuffer(int capacity) {
			return (PooledDataBuffer) createDataBufferFactory().allocateBuffer(capacity);
		}

		@Test
		default void retainAndRelease() {
			PooledDataBuffer buffer = createDataBuffer(1);
			buffer.write((byte) 'a');

			buffer.retain();
			assertThat(buffer.release()).isFalse();
			assertThat(buffer.release()).isTrue();
		}

		@Test
		default void tooManyReleases() {
			PooledDataBuffer buffer = createDataBuffer(1);
			buffer.write((byte) 'a');

			buffer.release();
			assertThatIllegalStateException().isThrownBy(buffer::release);
		}

	}

}
