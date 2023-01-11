package org.springframework.messaging.rsocket;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.plugins.RSocketInterceptor;
import io.rsocket.util.RSocketProxy;
import reactor.core.publisher.Mono;

/**
 * Intercept received RSockets and count successfully completed requests seen
 * on the server side. This is useful for verifying fire-and-forget
 * interactions.
 *
 * @author Rossen Stoyanchev
 */
class FireAndForgetCountingInterceptor extends AbstractRSocket implements RSocketInterceptor {

	private final List<CountingDecorator> rsockets = new CopyOnWriteArrayList<>();


	public int getRSocketCount() {
		return this.rsockets.size();
	}

	public int getFireAndForgetCount(int index) {
		return this.rsockets.get(index).getFireAndForgetCount();
	}


	@Override
	public RSocket apply(RSocket rsocket) {
		CountingDecorator decorator = new CountingDecorator(rsocket);
		this.rsockets.add(decorator);
		return decorator;
	}


	private static class CountingDecorator extends RSocketProxy {

		private final AtomicInteger fireAndForget = new AtomicInteger(0);

		CountingDecorator(RSocket delegate) {
			super(delegate);
		}

		public int getFireAndForgetCount() {
			return this.fireAndForget.get();
		}

		@Override
		public Mono<Void> fireAndForget(Payload payload) {
			return super.fireAndForget(payload).doOnSuccess(aVoid -> this.fireAndForget.incrementAndGet());
		}
	}

}
