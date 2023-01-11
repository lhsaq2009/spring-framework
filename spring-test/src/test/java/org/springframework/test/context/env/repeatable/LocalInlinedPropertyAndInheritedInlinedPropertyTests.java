package org.springframework.test.context.env.repeatable;

import org.junit.jupiter.api.Test;

import org.springframework.test.context.TestPropertySource;

/**
 * Integration tests for {@link TestPropertySource @TestPropertySource} as a
 * repeatable annotation.
 *
 * @author Anatoliy Korovin
 * @author Sam Brannen
 * @since 5.2
 */
@TestPropertySource(properties = "key2 = local")
class LocalInlinedPropertyAndInheritedInlinedPropertyTests extends AbstractClassWithTestProperty {

	@Test
	void test() {
		assertEnvironmentValue("key1", "parent");
		assertEnvironmentValue("key2", "local");
	}

}
