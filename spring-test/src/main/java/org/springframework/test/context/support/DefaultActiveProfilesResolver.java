package org.springframework.test.context.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ActiveProfilesResolver;
import org.springframework.test.util.MetaAnnotationUtils.AnnotationDescriptor;
import org.springframework.util.Assert;

import static org.springframework.test.util.MetaAnnotationUtils.findAnnotationDescriptor;

/**
 * Default implementation of the {@link ActiveProfilesResolver} strategy that
 * resolves <em>active bean definition profiles</em> based solely on profiles
 * configured declaratively via {@link ActiveProfiles#profiles} or
 * {@link ActiveProfiles#value}.
 *
 * @author Sam Brannen
 * @since 4.1
 * @see ActiveProfiles
 * @see ActiveProfilesResolver
 */
public class DefaultActiveProfilesResolver implements ActiveProfilesResolver {

	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	private static final Log logger = LogFactory.getLog(DefaultActiveProfilesResolver.class);


	/**
	 * Resolve the <em>bean definition profiles</em> for the given {@linkplain
	 * Class test class} based on profiles configured declaratively via
	 * {@link ActiveProfiles#profiles} or {@link ActiveProfiles#value}.
	 * @param testClass the test class for which the profiles should be resolved;
	 * never {@code null}
	 * @return the list of bean definition profiles to use when loading the
	 * {@code ApplicationContext}; never {@code null}
	 */
	@Override
	public String[] resolve(Class<?> testClass) {
		Assert.notNull(testClass, "Class must not be null");
		AnnotationDescriptor<ActiveProfiles> descriptor = findAnnotationDescriptor(testClass, ActiveProfiles.class);

		if (descriptor == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(String.format(
					"Could not find an 'annotation declaring class' for annotation type [%s] and class [%s]",
					ActiveProfiles.class.getName(), testClass.getName()));
			}
			return EMPTY_STRING_ARRAY;
		}
		else {
			ActiveProfiles annotation = descriptor.synthesizeAnnotation();
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ActiveProfiles [%s] for declaring class [%s].", annotation,
					descriptor.getDeclaringClass().getName()));
			}
			return annotation.profiles();
		}
	}

}
