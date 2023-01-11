package org.springframework.test.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code ProfileValueSourceConfiguration} is a class-level annotation for use
 * with JUnit 4 which is used to specify what type of {@link ProfileValueSource}
 * to use when retrieving <em>profile values</em> configured via
 * {@link IfProfileValue @IfProfileValue}.
 *
 * <p>This annotation may be used as a <em>meta-annotation</em> to create custom
 * <em>composed annotations</em>.
 *
 * @author Sam Brannen
 * @since 2.5
 * @see ProfileValueSource
 * @see IfProfileValue
 * @see ProfileValueUtils
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ProfileValueSourceConfiguration {

	/**
	 * The type of {@link ProfileValueSource} to use when retrieving
	 * <em>profile values</em>.
	 *
	 * @see SystemProfileValueSource
	 */
	Class<? extends ProfileValueSource> value() default SystemProfileValueSource.class;

}
