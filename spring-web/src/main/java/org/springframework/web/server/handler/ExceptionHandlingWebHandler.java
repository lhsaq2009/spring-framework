package org.springframework.web.server.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import reactor.core.publisher.Mono;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.WebHandler;

/**
 * WebHandler decorator that invokes one or more {@link WebExceptionHandler WebExceptionHandlers}
 * after the delegate {@link WebHandler}.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 */
public class ExceptionHandlingWebHandler extends WebHandlerDecorator {

	private final List<WebExceptionHandler> exceptionHandlers;


	/**
	 * Create an {@code ExceptionHandlingWebHandler} for the given delegate.
	 * @param delegate the WebHandler delegate
	 * @param handlers the WebExceptionHandlers to apply
	 */
	public ExceptionHandlingWebHandler(WebHandler delegate, List<WebExceptionHandler> handlers) {
		super(delegate);
		List<WebExceptionHandler> handlersToUse = new ArrayList<>();
		handlersToUse.add(new CheckpointInsertingHandler());
		handlersToUse.addAll(handlers);
		this.exceptionHandlers = Collections.unmodifiableList(handlersToUse);
	}


	/**
	 * Return a read-only list of the configured exception handlers.
	 */
	public List<WebExceptionHandler> getExceptionHandlers() {
		return this.exceptionHandlers;
	}


	@Override
	public Mono<Void> handle(ServerWebExchange exchange) {
		Mono<Void> completion;
		try {
			completion = super.handle(exchange);
		}
		catch (Throwable ex) {
			completion = Mono.error(ex);
		}

		for (WebExceptionHandler handler : this.exceptionHandlers) {
			completion = completion.onErrorResume(ex -> handler.handle(exchange, ex));
		}
		return completion;
	}


	/**
	 * WebExceptionHandler to insert a checkpoint with current URL information.
	 * Must be the first in order to ensure we catch the error signal before
	 * the exception is handled and e.g. turned into an error response.
	 * @since 5.2
 	 */
	private static class CheckpointInsertingHandler implements WebExceptionHandler {

		@Override
		public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
			ServerHttpRequest request = exchange.getRequest();
			String rawQuery = request.getURI().getRawQuery();
			String query = StringUtils.hasText(rawQuery) ? "?" + rawQuery : "";
			HttpMethod httpMethod = request.getMethod();
			String description = "HTTP " + httpMethod + " \"" + request.getPath() + query + "\"";
			return Mono.<Void>error(ex).checkpoint(description + " [ExceptionHandlingWebHandler]");
		}
	}

}
