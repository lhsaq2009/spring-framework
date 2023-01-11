package org.springframework.web.client;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Base class for exceptions thrown by {@link RestTemplate} in case a request
 * fails because of a server error response, as determined via
 * {@link ResponseErrorHandler#hasError(ClientHttpResponse)}, failure to decode
 * the response, or a low level I/O error.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public class RestClientException extends NestedRuntimeException {

	private static final long serialVersionUID = -4084444984163796577L;


	/**
	 * Construct a new instance of {@code RestClientException} with the given message.
	 * @param msg the message
	 */
	public RestClientException(String msg) {
		super(msg);
	}

	/**
	 * Construct a new instance of {@code RestClientException} with the given message and
	 * exception.
	 * @param msg the message
	 * @param ex the exception
	 */
	public RestClientException(String msg, Throwable ex) {
		super(msg, ex);
	}

}
