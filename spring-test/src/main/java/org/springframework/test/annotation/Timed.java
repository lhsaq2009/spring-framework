package org.springframework.test.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test annotation for use with JUnit 4 to indicate that a test method has to finish
 * execution in a {@linkplain #millis() specified time period}.
 *
 * <p>If the text execution takes longer than the specified time period, then
 * the test is considered to have failed.
 *
 * <p>Note that the time period includes execution of the test method itself,
 * any {@linkplain Repeat repetitions} of the test, and any <em>set up</em> or
 * <em>tear down</em> of the test fixture.
 *
 * <p>This annotation may be used as a <em>meta-annotation</em> to create custom
 * <em>composed annotations</em>.
 *
 * @author Rod Johnson
 * @author Sam Brannen
 * @since 2.0
 * @see org.springframework.test.annotation.Repeat
 * @see org.springframework.test.context.junit4.SpringJUnit4ClassRunner
 * @see org.springframework.test.context.junit4.rules.SpringMethodRule
 * @see org.springframework.test.context.junit4.statements.SpringFailOnTimeout
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Timed {

	/**
	 * The maximum amount of time (in milliseconds) that a test execution can
	 * take without being marked as failed due to taking too long.
	 */
	long millis();

}
