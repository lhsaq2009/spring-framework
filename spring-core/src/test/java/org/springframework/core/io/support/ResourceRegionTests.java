package org.springframework.core.io.support;

import org.junit.jupiter.api.Test;

import org.springframework.core.io.Resource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link ResourceRegion} class.
 *
 * @author Brian Clozel
 */
class ResourceRegionTests {

	@Test
	void shouldThrowExceptionWithNullResource() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new ResourceRegion(null, 0, 1));
	}

	@Test
	void shouldThrowExceptionForNegativePosition() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new ResourceRegion(mock(Resource.class), -1, 1));
	}

	@Test
	void shouldThrowExceptionForNegativeCount() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new ResourceRegion(mock(Resource.class), 0, -1));
	}

}
