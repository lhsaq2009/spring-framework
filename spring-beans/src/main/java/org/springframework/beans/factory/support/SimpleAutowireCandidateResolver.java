package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.lang.Nullable;

/**
 * {@link AutowireCandidateResolver} implementation to use when no annotation
 * support is available. This implementation checks the bean definition only.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @since 2.5
 */
public class SimpleAutowireCandidateResolver implements AutowireCandidateResolver {

	/**
	 * Shared instance of {@code SimpleAutowireCandidateResolver}.
	 * @since 5.2.7
	 */
	public static final SimpleAutowireCandidateResolver INSTANCE = new SimpleAutowireCandidateResolver();


	@Override
	public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		return bdHolder.getBeanDefinition().isAutowireCandidate();
	}

	@Override
	public boolean isRequired(DependencyDescriptor descriptor) {
		return descriptor.isRequired();
	}

	@Override
	public boolean hasQualifier(DependencyDescriptor descriptor) {
		return false;
	}

	@Override
	@Nullable
	public Object getSuggestedValue(DependencyDescriptor descriptor) {
		return null;
	}

	@Override
	@Nullable
	public Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, @Nullable String beanName) {
		return null;
	}

	/**
	 * This implementation returns {@code this} as-is.
	 * @see #INSTANCE
	 */
	@Override
	public AutowireCandidateResolver cloneIfNecessary() {
		return this;
	}

}
