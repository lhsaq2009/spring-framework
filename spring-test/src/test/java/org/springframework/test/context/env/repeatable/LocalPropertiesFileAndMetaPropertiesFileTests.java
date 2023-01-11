package org.springframework.test.context.env.repeatable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.env.repeatable.LocalPropertiesFileAndMetaPropertiesFileTests.MetaFileTestProperty;

/**
 * Integration tests for {@link TestPropertySource @TestPropertySource} as a
 * repeatable annotation.
 *
 * <p>Verify a property value is defined both in the properties file which is declared
 * via {@link MetaFileTestProperty @MetaFileTestProperty} and in the properties file
 * which is declared locally via {@code @TestPropertySource}.
 *
 * @author Anatoliy Korovin
 * @author Sam Brannen
 * @since 5.2
 */
@TestPropertySource("local.properties")
@MetaFileTestProperty
class LocalPropertiesFileAndMetaPropertiesFileTests extends AbstractRepeatableTestPropertySourceTests {

	@Test
	void test() {
		assertEnvironmentValue("key1", "local file");
		assertEnvironmentValue("key2", "meta file");
	}


	/**
	 * Composed annotation that declares a properties file via
	 * {@link TestPropertySource @TestPropertySource}.
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@TestPropertySource("meta.properties")
	@interface MetaFileTestProperty {
	}

}
