package org.springframework.util.comparator;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Test for {@link CompoundComparator}.
 *
 * @author Keith Donald
 * @author Chris Beams
 * @author Phillip Webb
 */
@Deprecated
class CompoundComparatorTests {

	@Test
	void shouldNeedAtLeastOneComparator() {
		Comparator<String> c = new CompoundComparator<>();
		assertThatIllegalStateException().isThrownBy(() ->
				c.compare("foo", "bar"));
	}

}
