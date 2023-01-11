package org.springframework.test.context.env;

import org.springframework.test.context.TestPropertySource;

/**
 * Integration tests for {@link TestPropertySource @TestPropertySource}
 * support with an explicitly named properties file in the classpath.
 *
 * @author Sam Brannen
 * @since 4.1
 */
@TestPropertySource("explicit.properties")
public class ExplicitPropertiesFileInClasspathTestPropertySourceTests extends AbstractExplicitPropertiesFileTests {
}
