package org.springframework.web.reactive.function.client

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher
import org.springframework.core.ParameterizedTypeReference
import reactor.core.publisher.Mono
import java.util.concurrent.CompletableFuture

/**
 * Mock object based tests for [WebClient] Kotlin extensions
 *
 * @author Sebastien Deleuze
 */
class WebClientExtensionsTests {

	private val requestBodySpec = mockk<WebClient.RequestBodySpec>(relaxed = true)

	private val responseSpec = mockk<WebClient.ResponseSpec>(relaxed = true)


	@Test
	fun `RequestBodySpec#body with Publisher and reified type parameters`() {
		val body = mockk<Publisher<List<Foo>>>()
		requestBodySpec.body(body)
		verify { requestBodySpec.body(body, object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun `RequestBodySpec#body with Flow and reified type parameters`() {
		val body = mockk<Flow<List<Foo>>>()
		requestBodySpec.body(body)
		verify { requestBodySpec.body(ofType<Any>(), object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun `RequestBodySpec#body with CompletableFuture and reified type parameters`() {
		val body = mockk<CompletableFuture<List<Foo>>>()
		requestBodySpec.body<List<Foo>>(body)
		verify { requestBodySpec.body(ofType<Any>(), object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun `ResponseSpec#bodyToMono with reified type parameters`() {
		responseSpec.bodyToMono<List<Foo>>()
		verify { responseSpec.bodyToMono(object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun `ResponseSpec#bodyToFlux with reified type parameters`() {
		responseSpec.bodyToFlux<List<Foo>>()
		verify { responseSpec.bodyToFlux(object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun `bodyToFlow with reified type parameters`() {
		responseSpec.bodyToFlow<List<Foo>>()
		verify { responseSpec.bodyToFlux(object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun awaitExchange() {
		val response = mockk<ClientResponse>()
		every { requestBodySpec.exchange() } returns Mono.just(response)
		runBlocking {
			assertThat(requestBodySpec.awaitExchange()).isEqualTo(response)
		}
	}

	@Test
	fun awaitBody() {
		val spec = mockk<WebClient.ResponseSpec>()
		every { spec.bodyToMono<String>() } returns Mono.just("foo")
		runBlocking {
			assertThat(spec.awaitBody<String>()).isEqualTo("foo")
		}
	}

	class Foo
}
