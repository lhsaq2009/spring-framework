package org.springframework.core.testfixture;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Tests for {@link TestGroup} parsing.
 *
 * @author Phillip Webb
 * @author Sam Brannen
 */
class TestGroupParsingTests {

	@Test
	void parseNull() {
		assertThat(TestGroup.parse(null)).isEqualTo(Collections.emptySet());
	}

	@Test
	void parseEmptyString() {
		assertThat(TestGroup.parse("")).isEqualTo(Collections.emptySet());
	}

	@Test
	void parseBlankString() {
		assertThat(TestGroup.parse("     ")).isEqualTo(Collections.emptySet());
	}

	@Test
	void parseWithSpaces() {
		assertThat(TestGroup.parse(" PERFORMANCE,  PERFORMANCE ")).containsOnly(TestGroup.PERFORMANCE);
	}

	@Test
	void parseInMixedCase() {
		assertThat(TestGroup.parse("performance,  PERFormaNCE")).containsOnly(TestGroup.PERFORMANCE);
	}

	@Test
	void parseMissing() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> TestGroup.parse("performance, missing"))
			.withMessageContaining("Unable to find test group 'missing' when parsing " +
					"testGroups value: 'performance, missing'. Available groups include: " +
					"[LONG_RUNNING,PERFORMANCE]");
	}

	@Test
	void parseAll() {
		assertThat(TestGroup.parse("all")).isEqualTo(EnumSet.allOf(TestGroup.class));
	}

	@Test
	void parseAllExceptPerformance() {
		Set<TestGroup> expected = EnumSet.allOf(TestGroup.class);
		expected.remove(TestGroup.PERFORMANCE);
		assertThat(TestGroup.parse("all-performance")).isEqualTo(expected);
	}

	@Test
	void parseAllExceptMissing() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> TestGroup.parse("all-missing"))
			.withMessageContaining("Unable to find test group 'missing' when parsing " +
					"testGroups value: 'all-missing'. Available groups include: " +
					"[LONG_RUNNING,PERFORMANCE]");
	}

}
