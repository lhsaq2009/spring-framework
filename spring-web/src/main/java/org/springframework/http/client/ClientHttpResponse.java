package org.springframework.http.client;

import java.io.Closeable;
import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;

/**
 * Represents a client-side HTTP response.
 *
 * <p>Obtained via an invocation of {@link ClientHttpRequest#execute()}.
 *
 * <p>A {@code ClientHttpResponse} must be {@linkplain #close() closed},
 * typically in a {@code finally} block.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public interface ClientHttpResponse extends HttpInputMessage, Closeable {

	/**
	 * Get the HTTP status code as an {@link HttpStatus} enum value.
	 * <p>For status codes not supported by {@code HttpStatus}, use
	 * {@link #getRawStatusCode()} instead.
	 * @return the HTTP status as an HttpStatus enum value (never {@code null})
	 * @throws IOException in case of I/O errors
	 * @throws IllegalArgumentException in case of an unknown HTTP status code
	 * @since #getRawStatusCode()
	 * @see HttpStatus#valueOf(int)
	 */
	HttpStatus getStatusCode() throws IOException;

	/**
	 * Get the HTTP status code (potentially non-standard and not
	 * resolvable through the {@link HttpStatus} enum) as an integer.
	 * @return the HTTP status as an integer value
	 * @throws IOException in case of I/O errors
	 * @since 3.1.1
	 * @see #getStatusCode()
	 * @see HttpStatus#resolve(int)
	 */
	int getRawStatusCode() throws IOException;

	/**
	 * Get the HTTP status text of the response.
	 * @return the HTTP status text
	 * @throws IOException in case of I/O errors
	 */
	String getStatusText() throws IOException;

	/**
	 * Close this response, freeing any resources created.
	 */
	@Override
	void close();

}
