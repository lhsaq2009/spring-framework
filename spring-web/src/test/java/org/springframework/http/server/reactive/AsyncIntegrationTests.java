package org.springframework.http.server.reactive;

import java.net.URI;
import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.testfixture.http.server.reactive.bootstrap.AbstractHttpHandlerIntegrationTests;
import org.springframework.web.testfixture.http.server.reactive.bootstrap.HttpServer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Stephane Maldini
 * @since 5.0
 */
class AsyncIntegrationTests extends AbstractHttpHandlerIntegrationTests {

	private final Scheduler asyncGroup = Schedulers.parallel();

	private final DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();


	@Override
	protected AsyncHandler createHttpHandler() {
		return new AsyncHandler();
	}

	@ParameterizedHttpServerTest
	void basicTest(HttpServer httpServer) throws Exception {
		startServer(httpServer);

		URI url = new URI("http://localhost:" + port);
		ResponseEntity<String> response = new RestTemplate().exchange(RequestEntity.get(url).build(), String.class);

		assertThat(response.getBody()).isEqualTo("hello");
	}


	private class AsyncHandler implements HttpHandler {

		@Override
		public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
			return response.writeWith(Flux.just("h", "e", "l", "l", "o")
										.delayElements(Duration.ofMillis(100))
										.publishOn(asyncGroup)
					.collect(dataBufferFactory::allocateBuffer, (buffer, str) -> buffer.write(str.getBytes())));
		}
	}

}
