package org.springframework.util.comparator;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;


/**
 * Tests for {@link InvertibleComparator}.
 *
 * @author Keith Donald
 * @author Chris Beams
 * @author Phillip Webb
 */
@Deprecated
class InvertibleComparatorTests {

	private final Comparator<Integer> comparator = new ComparableComparator<>();


	@Test
	void shouldNeedComparator() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new InvertibleComparator<>(null));
	}

	@Test
	void shouldNeedComparatorWithAscending() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				new InvertibleComparator<>(null, true));
	}

	@Test
	void shouldDefaultToAscending() throws Exception {
		InvertibleComparator<Integer> invertibleComparator = new InvertibleComparator<>(comparator);
		assertThat(invertibleComparator.isAscending()).isTrue();
		assertThat(invertibleComparator.compare(1, 2)).isEqualTo(-1);
	}

	@Test
	void shouldInvert() throws Exception {
		InvertibleComparator<Integer> invertibleComparator = new InvertibleComparator<>(comparator);
		assertThat(invertibleComparator.isAscending()).isTrue();
		assertThat(invertibleComparator.compare(1, 2)).isEqualTo(-1);
		invertibleComparator.invertOrder();
		assertThat(invertibleComparator.isAscending()).isFalse();
		assertThat(invertibleComparator.compare(1, 2)).isEqualTo(1);
	}

	@Test
	void shouldCompareAscending() throws Exception {
		InvertibleComparator<Integer> invertibleComparator = new InvertibleComparator<>(comparator, true);
		assertThat(invertibleComparator.compare(1, 2)).isEqualTo(-1);
	}

	@Test
	void shouldCompareDescending() throws Exception {
		InvertibleComparator<Integer> invertibleComparator = new InvertibleComparator<>(comparator, false);
		assertThat(invertibleComparator.compare(1, 2)).isEqualTo(1);
	}

}
