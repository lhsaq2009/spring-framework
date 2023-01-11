package org.springframework.http.client.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Base class for {@link org.springframework.web.client.RestTemplate}
 * and other HTTP accessing gateway helpers, adding interceptor-related
 * properties to {@link HttpAccessor}'s common properties.
 *
 * <p>Not intended to be used directly.
 * See {@link org.springframework.web.client.RestTemplate} for an entry point.
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @since 3.0
 * @see ClientHttpRequestInterceptor
 * @see InterceptingClientHttpRequestFactory
 * @see org.springframework.web.client.RestTemplate
 */
public abstract class InterceptingHttpAccessor extends HttpAccessor {

	private final List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

	@Nullable
	private volatile ClientHttpRequestFactory interceptingRequestFactory;


	/**
	 * Set the request interceptors that this accessor should use.
	 * <p>The interceptors will get immediately sorted according to their
	 * {@linkplain AnnotationAwareOrderComparator#sort(List) order}.
	 * @see #getRequestFactory()
	 * @see AnnotationAwareOrderComparator
	 */
	public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
		Assert.noNullElements(interceptors, "'interceptors' must not contain null elements");
		// Take getInterceptors() List as-is when passed in here
		if (this.interceptors != interceptors) {
			this.interceptors.clear();
			this.interceptors.addAll(interceptors);
			AnnotationAwareOrderComparator.sort(this.interceptors);
		}
	}

	/**
	 * Get the request interceptors that this accessor uses.
	 * <p>The returned {@link List} is active and may be modified. Note,
	 * however, that the interceptors will not be resorted according to their
	 * {@linkplain AnnotationAwareOrderComparator#sort(List) order} before the
	 * {@link ClientHttpRequestFactory} is built.
	 */
	public List<ClientHttpRequestInterceptor> getInterceptors() {
		return this.interceptors;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
		super.setRequestFactory(requestFactory);
		this.interceptingRequestFactory = null;
	}

	/**
	 * Overridden to expose an {@link InterceptingClientHttpRequestFactory}
	 * if necessary.
	 * @see #getInterceptors()
	 */
	@Override
	public ClientHttpRequestFactory getRequestFactory() {
		List<ClientHttpRequestInterceptor> interceptors = getInterceptors();
		if (!CollectionUtils.isEmpty(interceptors)) {
			ClientHttpRequestFactory factory = this.interceptingRequestFactory;
			if (factory == null) {
				factory = new InterceptingClientHttpRequestFactory(super.getRequestFactory(), interceptors);
				this.interceptingRequestFactory = factory;
			}
			return factory;
		}
		else {
			return super.getRequestFactory();
		}
	}

}
