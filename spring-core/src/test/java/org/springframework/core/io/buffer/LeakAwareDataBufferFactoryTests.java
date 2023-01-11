package org.springframework.core.io.buffer;

import org.junit.jupiter.api.Test;

import org.springframework.core.testfixture.io.buffer.LeakAwareDataBufferFactory;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.core.io.buffer.DataBufferUtils.release;

/**
 * @author Arjen Poutsma
 */
class LeakAwareDataBufferFactoryTests {

	private final LeakAwareDataBufferFactory bufferFactory = new LeakAwareDataBufferFactory();


	@Test
	void leak() {
		DataBuffer dataBuffer = this.bufferFactory.allocateBuffer();
		try {
			assertThatExceptionOfType(AssertionError.class).isThrownBy(
					this.bufferFactory::checkForLeaks);
		}
		finally {
			release(dataBuffer);
		}
	}

	@Test
	void noLeak() {
		DataBuffer dataBuffer = this.bufferFactory.allocateBuffer();
		release(dataBuffer);
		this.bufferFactory.checkForLeaks();
	}

}
