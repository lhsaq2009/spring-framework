package org.springframework.test.context;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @TestPropertySources} is a container for one or more
 * {@link TestPropertySource @TestPropertySource} declarations.
 *
 * <p>Note, however, that use of the {@code @TestPropertySources} container is
 * completely optional since {@code @TestPropertySource} is a
 * {@linkplain java.lang.annotation.Repeatable repeatable} annotation.
 *
 * @author Anatoliy Korovin
 * @author Sam Brannen
 * @since 5.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TestPropertySources {

	/**
	 * An array of one or more {@link TestPropertySource @TestPropertySource}
	 * declarations.
	 */
	TestPropertySource[] value();

}
