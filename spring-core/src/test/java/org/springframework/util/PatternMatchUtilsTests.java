package org.springframework.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 * @author Johan Gorter
 */
class PatternMatchUtilsTests {

	@Test
	void trivial() {
		assertThat(PatternMatchUtils.simpleMatch((String) null, "")).isEqualTo(false);
		assertThat(PatternMatchUtils.simpleMatch("1", null)).isEqualTo(false);
		doTest("*", "123", true);
		doTest("123", "123", true);
	}

	@Test
	void startsWith() {
		doTest("get*", "getMe", true);
		doTest("get*", "setMe", false);
	}

	@Test
	void endsWith() {
		doTest("*Test", "getMeTest", true);
		doTest("*Test", "setMe", false);
	}

	@Test
	void between() {
		doTest("*stuff*", "getMeTest", false);
		doTest("*stuff*", "getstuffTest", true);
		doTest("*stuff*", "stuffTest", true);
		doTest("*stuff*", "getstuff", true);
		doTest("*stuff*", "stuff", true);
	}

	@Test
	void startsEnds() {
		doTest("on*Event", "onMyEvent", true);
		doTest("on*Event", "onEvent", true);
		doTest("3*3", "3", false);
		doTest("3*3", "33", true);
	}

	@Test
	void startsEndsBetween() {
		doTest("12*45*78", "12345678", true);
		doTest("12*45*78", "123456789", false);
		doTest("12*45*78", "012345678", false);
		doTest("12*45*78", "124578", true);
		doTest("12*45*78", "1245457878", true);
		doTest("3*3*3", "33", false);
		doTest("3*3*3", "333", true);
	}

	@Test
	void ridiculous() {
		doTest("*1*2*3*", "0011002001010030020201030", true);
		doTest("1*2*3*4", "10300204", false);
		doTest("1*2*3*3", "10300203", false);
		doTest("*1*2*3*", "123", true);
		doTest("*1*2*3*", "132", false);
	}

	@Test
	void patternVariants() {
		doTest("*a", "*", false);
		doTest("*a", "a", true);
		doTest("*a", "b", false);
		doTest("*a", "aa", true);
		doTest("*a", "ba", true);
		doTest("*a", "ab", false);
		doTest("**a", "*", false);
		doTest("**a", "a", true);
		doTest("**a", "b", false);
		doTest("**a", "aa", true);
		doTest("**a", "ba", true);
		doTest("**a", "ab", false);
	}

	private void doTest(String pattern, String str, boolean shouldMatch) {
		assertThat(PatternMatchUtils.simpleMatch(pattern, str)).isEqualTo(shouldMatch);
	}

}
