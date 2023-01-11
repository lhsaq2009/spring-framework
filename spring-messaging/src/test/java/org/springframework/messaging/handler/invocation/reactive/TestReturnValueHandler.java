package org.springframework.messaging.handler.invocation.reactive;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;

/**
 * Return value handler that simply stores the last return value.
 * @author Rossen Stoyanchev
 */
public class TestReturnValueHandler implements HandlerMethodReturnValueHandler {

	@Nullable
	private Object lastReturnValue;


	@Nullable
	public Object getLastReturnValue() {
		return this.lastReturnValue;
	}


	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return true;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Mono<Void> handleReturnValue(@Nullable Object value, MethodParameter returnType, Message<?> message) {
		return value instanceof Publisher ?
				new ChannelSendOperator((Publisher) value, this::saveValue) :
				saveValue(value);
	}

	private Mono<Void> saveValue(@Nullable Object value) {
		this.lastReturnValue = value;
		return Mono.empty();
	}

}
