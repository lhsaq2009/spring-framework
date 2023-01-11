package org.springframework.core.testfixture;

import org.apache.commons.logging.Log;

import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Utility methods that allow JUnit tests to assume certain conditions hold
 * {@code true}. If an assumption fails, it means the test should be aborted.
 *
 * @author Rob Winch
 * @author Phillip Webb
 * @author Sam Brannen
 * @since 3.2
 * @see #notLogging(Log)
 * @see EnabledForTestGroups @EnabledForTestGroups
 */
public abstract class Assume {

	/**
	 * Assume that the specified log is not set to Trace or Debug.
	 * @param log the log to test
	 * @throws org.opentest4j.TestAbortedException if the assumption fails
	 */
	public static void notLogging(Log log) {
		assumeFalse(log.isTraceEnabled());
		assumeFalse(log.isDebugEnabled());
	}

}
