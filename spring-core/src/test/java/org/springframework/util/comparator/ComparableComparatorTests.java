package org.springframework.util.comparator;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Tests for {@link ComparableComparator}.
 *
 * @author Keith Donald
 * @author Chris Beams
 * @author Phillip Webb
 */
class ComparableComparatorTests {

	@Test
	void comparableComparator() {
		Comparator<String> c = new ComparableComparator<>();
		String s1 = "abc";
		String s2 = "cde";
		assertThat(c.compare(s1, s2) < 0).isTrue();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void shouldNeedComparable() {
		Comparator c = new ComparableComparator();
		Object o1 = new Object();
		Object o2 = new Object();
		assertThatExceptionOfType(ClassCastException.class).isThrownBy(() ->
				c.compare(o1, o2));
	}

}
