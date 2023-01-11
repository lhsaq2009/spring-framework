package org.springframework.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Test case for {@link CompositeIterator}.
 *
 * @author Erwin Vervaet
 * @author Juergen Hoeller
 */
class CompositeIteratorTests {

	@Test
	void noIterators() {
		CompositeIterator<String> it = new CompositeIterator<>();
		assertThat(it.hasNext()).isFalse();
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
				it::next);
	}

	@Test
	void singleIterator() {
		CompositeIterator<String> it = new CompositeIterator<>();
		it.add(Arrays.asList("0", "1").iterator());
		for (int i = 0; i < 2; i++) {
			assertThat(it.hasNext()).isTrue();
			assertThat(it.next()).isEqualTo(String.valueOf(i));
		}
		assertThat(it.hasNext()).isFalse();
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
				it::next);
	}

	@Test
	void multipleIterators() {
		CompositeIterator<String> it = new CompositeIterator<>();
		it.add(Arrays.asList("0", "1").iterator());
		it.add(Arrays.asList("2").iterator());
		it.add(Arrays.asList("3", "4").iterator());
		for (int i = 0; i < 5; i++) {
			assertThat(it.hasNext()).isTrue();
			assertThat(it.next()).isEqualTo(String.valueOf(i));
		}
		assertThat(it.hasNext()).isFalse();

		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
				it::next);
	}

	@Test
	void inUse() {
		List<String> list = Arrays.asList("0", "1");
		CompositeIterator<String> it = new CompositeIterator<>();
		it.add(list.iterator());
		it.hasNext();
		assertThatIllegalStateException().isThrownBy(() ->
				it.add(list.iterator()));
		CompositeIterator<String> it2 = new CompositeIterator<>();
		it2.add(list.iterator());
		it2.next();
		assertThatIllegalStateException().isThrownBy(() ->
				it2.add(list.iterator()));
	}

	@Test
	void duplicateIterators() {
		List<String> list = Arrays.asList("0", "1");
		Iterator<String> iterator = list.iterator();
		CompositeIterator<String> it = new CompositeIterator<>();
		it.add(iterator);
		it.add(list.iterator());
		assertThatIllegalArgumentException().isThrownBy(() ->
				it.add(iterator));
	}

}
