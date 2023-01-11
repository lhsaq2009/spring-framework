package org.springframework.util;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Stephane Nicoll
 */
class ExceptionTypeFilterTests {

	@Test
	void subClassMatch() {
		ExceptionTypeFilter filter = new ExceptionTypeFilter(asList(RuntimeException.class), null, true);
		assertThat(filter.match(RuntimeException.class)).isTrue();
		assertThat(filter.match(IllegalStateException.class)).isTrue();
	}

}
