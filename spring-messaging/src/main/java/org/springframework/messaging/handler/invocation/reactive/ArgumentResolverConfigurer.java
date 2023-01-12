package org.springframework.messaging.handler.invocation.reactive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Assist with configuration for handler method argument resolvers.
 * At present, it supports only providing a list of custom resolvers.
 *
 * @author Rossen Stoyanchev
 * @since 5.2
 */
public class ArgumentResolverConfigurer {

	private final List<HandlerMethodArgumentResolver> customResolvers = new ArrayList<>(8);


	/**
	 * Configure resolvers for custom handler method arguments.
	 * @param resolver the resolvers to add
	 */
	public void addCustomResolver(HandlerMethodArgumentResolver... resolver) {
		Assert.notNull(resolver, "'resolvers' must not be null");
		this.customResolvers.addAll(Arrays.asList(resolver));
	}


	public List<HandlerMethodArgumentResolver> getCustomResolvers() {
		return this.customResolvers;
	}

}
