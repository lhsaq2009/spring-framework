package org.springframework.util;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Stephane Nicoll
 */
class InstanceFilterTests {

	@Test
	void emptyFilterApplyMatchIfEmpty() {
		InstanceFilter<String> filter = new InstanceFilter<>(null, null, true);
		match(filter, "foo");
		match(filter, "bar");
	}

	@Test
	void includesFilter() {
		InstanceFilter<String> filter = new InstanceFilter<>(
				asList("First", "Second"), null, true);
		match(filter, "Second");
		doNotMatch(filter, "foo");
	}

	@Test
	void excludesFilter() {
		InstanceFilter<String> filter = new InstanceFilter<>(
				null, asList("First", "Second"), true);
		doNotMatch(filter, "Second");
		match(filter, "foo");
	}

	@Test
	void includesAndExcludesFilters() {
		InstanceFilter<String> filter = new InstanceFilter<>(
				asList("foo", "Bar"), asList("First", "Second"), true);
		doNotMatch(filter, "Second");
		match(filter, "foo");
	}

	@Test
	void includesAndExcludesFiltersConflict() {
		InstanceFilter<String> filter = new InstanceFilter<>(
				asList("First"), asList("First"), true);
		doNotMatch(filter, "First");
	}

	private <T> void match(InstanceFilter<T> filter, T candidate) {
		assertThat(filter.match(candidate)).as("filter '" + filter + "' should match " + candidate).isTrue();
	}

	private <T> void doNotMatch(InstanceFilter<T> filter, T candidate) {
		assertThat(filter.match(candidate)).as("filter '" + filter + "' should not match " + candidate).isFalse();
	}

}
