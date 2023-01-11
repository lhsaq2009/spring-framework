package org.springframework.test.context.env;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.UseTechnicalNames;
import org.junit.runner.RunWith;

import org.springframework.test.context.TestPropertySource;

/**
 * Test suite for tests that involve {@link TestPropertySource @TestPropertySource}.
 *
 * <p>Note that tests included in this suite will be executed at least twice if
 * run from an automated build process, test runner, etc. that is not configured
 * to exclude tests based on a {@code "*TestSuite.class"} pattern match.
 *
 * @author Sam Brannen
 * @since 5.2
 */
@RunWith(JUnitPlatform.class)
@IncludeEngines("junit-jupiter")
@SelectPackages("org.springframework.test.context.env")
@IncludeClassNamePatterns(".*Tests$")
@UseTechnicalNames
public class TestPropertySourceTestSuite {
}
