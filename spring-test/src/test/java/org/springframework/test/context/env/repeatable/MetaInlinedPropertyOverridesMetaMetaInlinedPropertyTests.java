package org.springframework.test.context.env.repeatable;

import org.junit.jupiter.api.Test;

import org.springframework.test.context.TestPropertySource;

/**
 * Integration tests for {@link TestPropertySource @TestPropertySource} as a
 * repeatable annotation.
 *
 * @author Sam Brannen
 * @since 5.2
 */
@MetaInlinedTestProperty
@MetaComposedTestProperty
class MetaInlinedPropertyOverridesMetaMetaInlinedPropertyTests extends AbstractRepeatableTestPropertySourceTests {

	@Test
	void test() {
		assertEnvironmentValue("enigma", "meta");
	}

}
