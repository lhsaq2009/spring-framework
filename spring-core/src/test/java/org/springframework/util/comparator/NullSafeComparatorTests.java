package org.springframework.util.comparator;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link NullSafeComparator}.
 *
 * @author Keith Donald
 * @author Chris Beams
 * @author Phillip Webb
 */
class NullSafeComparatorTests {

	@SuppressWarnings("unchecked")
	@Test
	void shouldCompareWithNullsLow() {
		Comparator<String> c = NullSafeComparator.NULLS_LOW;
		assertThat(c.compare(null, "boo") < 0).isTrue();
	}

	@SuppressWarnings("unchecked")
	@Test
	void shouldCompareWithNullsHigh() {
		Comparator<String> c = NullSafeComparator.NULLS_HIGH;
		assertThat(c.compare(null, "boo") > 0).isTrue();
		assertThat(c.compare(null, null) == 0).isTrue();
	}

}
