package org.springframework.web.reactive.function.server;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.testfixture.http.server.reactive.MockServerHttpRequest;
import org.springframework.web.testfixture.server.MockServerWebExchange;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * Unit tests for {@link DefaultServerRequestBuilder}.
 *
 * @author Arjen Poutsma
 * @author Sam Brannen
 */
public class DefaultServerRequestBuilderTests {

	private final DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();


	@Test
	public void from() {
		MockServerHttpRequest request = MockServerHttpRequest.post("https://example.com")
				.header("foo", "bar")
				.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		ServerRequest other =
				ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
		other.attributes().put("attr1", "value1");

		Flux<DataBuffer> body = Flux.just("baz")
				.map(s -> s.getBytes(StandardCharsets.UTF_8))
				.map(dataBufferFactory::wrap);

		ServerRequest result = ServerRequest.from(other)
				.method(HttpMethod.HEAD)
				.headers(httpHeaders -> httpHeaders.set("foo", "baar"))
				.cookies(cookies -> cookies.set("baz", ResponseCookie.from("baz", "quux").build()))
				.attribute("attr2", "value2")
				.attributes(attributes -> attributes.put("attr3", "value3"))
				.body(body)
				.build();

		assertThat(result.method()).isEqualTo(HttpMethod.HEAD);
		assertThat(result.headers().asHttpHeaders()).hasSize(1);
		assertThat(result.headers().asHttpHeaders().getFirst("foo")).isEqualTo("baar");
		assertThat(result.cookies()).hasSize(1);
		assertThat(result.cookies().getFirst("baz").getValue()).isEqualTo("quux");
		assertThat(result.attributes()).containsOnlyKeys(ServerWebExchange.LOG_ID_ATTRIBUTE, "attr1", "attr2", "attr3");
		assertThat(result.attributes()).contains(entry("attr1", "value1"), entry("attr2", "value2"), entry("attr3", "value3"));

		StepVerifier.create(result.bodyToFlux(String.class))
				.expectNext("baz")
				.verifyComplete();
	}

}
