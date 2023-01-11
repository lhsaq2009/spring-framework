package org.springframework.web.server;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Exception for errors that fit response status 405 (method not allowed).
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 */
@SuppressWarnings("serial")
public class MethodNotAllowedException extends ResponseStatusException {

	private final String method;

	private final Set<HttpMethod> httpMethods;


	public MethodNotAllowedException(HttpMethod method, Collection<HttpMethod> supportedMethods) {
		this(method.name(), supportedMethods);
	}

	public MethodNotAllowedException(String method, @Nullable Collection<HttpMethod> supportedMethods) {
		super(HttpStatus.METHOD_NOT_ALLOWED, "Request method '" + method + "' not supported");
		Assert.notNull(method, "'method' is required");
		if (supportedMethods == null) {
			supportedMethods = Collections.emptySet();
		}
		this.method = method;
		this.httpMethods = Collections.unmodifiableSet(new LinkedHashSet<>(supportedMethods));
	}


	/**
	 * Return a Map with an "Allow" header.
	 * @since 5.1.11
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Map<String, String> getHeaders() {
		return getResponseHeaders().toSingleValueMap();
	}

	/**
	 * Return HttpHeaders with an "Allow" header.
	 * @since 5.1.13
	 */
	@Override
	public HttpHeaders getResponseHeaders() {
		if (CollectionUtils.isEmpty(this.httpMethods)) {
			return HttpHeaders.EMPTY;
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setAllow(this.httpMethods);
		return headers;
	}

	/**
	 * Return the HTTP method for the failed request.
	 */
	public String getHttpMethod() {
		return this.method;
	}

	/**
	 * Return the list of supported HTTP methods.
	 */
	public Set<HttpMethod> getSupportedMethods() {
		return this.httpMethods;
	}

}
