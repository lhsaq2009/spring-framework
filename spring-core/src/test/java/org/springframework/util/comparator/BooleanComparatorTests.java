package org.springframework.util.comparator;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Tests for {@link BooleanComparator}.
 *
 * @author Keith Donald
 * @author Chris Beams
 * @author Phillip Webb
 */
class BooleanComparatorTests {

	@Test
	void shouldCompareWithTrueLow() {
		Comparator<Boolean> c = new BooleanComparator(true);
		assertThat(c.compare(true, false)).isEqualTo(-1);
		assertThat(c.compare(Boolean.TRUE, Boolean.TRUE)).isEqualTo(0);
	}

	@Test
	void shouldCompareWithTrueHigh() {
		Comparator<Boolean> c = new BooleanComparator(false);
		assertThat(c.compare(true, false)).isEqualTo(1);
		assertThat(c.compare(Boolean.TRUE, Boolean.TRUE)).isEqualTo(0);
	}

	@Test
	void shouldCompareFromTrueLow() {
		Comparator<Boolean> c = BooleanComparator.TRUE_LOW;
		assertThat(c.compare(true, false)).isEqualTo(-1);
		assertThat(c.compare(Boolean.TRUE, Boolean.TRUE)).isEqualTo(0);
	}

	@Test
	void shouldCompareFromTrueHigh() {
		Comparator<Boolean> c = BooleanComparator.TRUE_HIGH;
		assertThat(c.compare(true, false)).isEqualTo(1);
		assertThat(c.compare(Boolean.TRUE, Boolean.TRUE)).isEqualTo(0);
	}

}
