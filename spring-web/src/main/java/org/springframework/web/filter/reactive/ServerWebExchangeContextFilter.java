package org.springframework.web.filter.reactive;

import java.util.Optional;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

/**
 * Inserts an attribute in the Reactor {@link Context} that makes the current
 * {@link ServerWebExchange} available under the attribute name
 * {@link #EXCHANGE_CONTEXT_ATTRIBUTE}. This is useful for access to the
 * exchange without explicitly passing it to components that participate in
 * request processing.
 *
 * <p>The convenience method {@link #get(Context)} looks up the exchange.
 *
 * @author Rossen Stoyanchev
 * @since 5.2
 */
public class ServerWebExchangeContextFilter implements WebFilter {

	/** Attribute name under which the exchange is saved in the context. */
	public static final String EXCHANGE_CONTEXT_ATTRIBUTE =
			ServerWebExchangeContextFilter.class.getName() + ".EXCHANGE_CONTEXT";


	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return chain.filter(exchange)
				.subscriberContext(cxt -> cxt.put(EXCHANGE_CONTEXT_ATTRIBUTE, exchange));
	}


	/**
	 * Access the {@link ServerWebExchange} from the Reactor Context, if available,
	 * which is if {@link ServerWebExchangeContextFilter} is configured for use
	 * and the give context was obtained from a request processing chain.
	 * @param context the context in which to access the exchange
	 * @return the exchange
	 */
	public static Optional<ServerWebExchange> get(Context context) {
		return context.getOrEmpty(EXCHANGE_CONTEXT_ATTRIBUTE);
	}

}
