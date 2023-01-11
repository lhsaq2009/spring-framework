package org.springframework.test.context.env.repeatable;

import org.junit.jupiter.api.Test;

import org.springframework.test.context.TestPropertySource;

/**
 * Integration tests for {@link TestPropertySource @TestPropertySource} as a
 * repeatable annotation.
 *
 * <p>Same as {@link ReversedExplicitPropertiesFilesRepeatedTestPropertySourceTests},
 * but with the order of the properties files reversed.
 *
 * @author Anatoliy Korovin
 * @author Sam Brannen
 * @since 5.2
 */
@TestPropertySource("first.properties")
@TestPropertySource("second.properties")
class ExplicitPropertiesFilesRepeatedTestPropertySourceTests extends AbstractRepeatableTestPropertySourceTests {

	@Test
	void test() {
		assertEnvironmentValue("alpha", "omega");
		assertEnvironmentValue("first", "1111");
		assertEnvironmentValue("second", "2222");
	}

}
