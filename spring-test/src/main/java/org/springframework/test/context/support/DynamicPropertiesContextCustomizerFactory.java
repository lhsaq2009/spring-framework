package org.springframework.test.context.support;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.test.context.DynamicPropertySource;

/**
 * {@link ContextCustomizerFactory} to support
 * {@link DynamicPropertySource @DynamicPropertySource} methods.
 *
 * @author Phillip Webb
 * @since 5.2.5
 * @see DynamicPropertiesContextCustomizer
 */
class DynamicPropertiesContextCustomizerFactory implements ContextCustomizerFactory {

	@Override
	@Nullable
	public DynamicPropertiesContextCustomizer createContextCustomizer(Class<?> testClass,
			List<ContextConfigurationAttributes> configAttributes) {

		Set<Method> methods = MethodIntrospector.selectMethods(testClass, this::isAnnotated);
		if (methods.isEmpty()) {
			return null;
		}
		return new DynamicPropertiesContextCustomizer(methods);
	}

	private boolean isAnnotated(Method method) {
		return MergedAnnotations.from(method).isPresent(DynamicPropertySource.class);
	}

}
