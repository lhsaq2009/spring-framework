package org.springframework.web.reactive.function.client

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono

/**
 * Mock object based tests for [ClientResponse] Kotlin extensions.
 *
 * @author Sebastien Deleuze
 */
class ClientResponseExtensionsTests {

	private val response = mockk<ClientResponse>(relaxed = true)

	@Test
	fun `bodyToMono with reified type parameters`() {
		response.bodyToMono<List<Foo>>()
		verify { response.bodyToMono(object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun `bodyToFlux with reified type parameters`() {
		response.bodyToFlux<List<Foo>>()
		verify { response.bodyToFlux(object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun `bodyToFlow with reified type parameters`() {
		response.bodyToFlow<List<Foo>>()
		verify { response.bodyToFlux(object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun `toEntity with reified type parameters`() {
		response.toEntity<List<Foo>>()
		verify { response.toEntity(object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun `ResponseSpec#toEntityList with reified type parameters`() {
		response.toEntityList<List<Foo>>()
		verify { response.toEntityList(object : ParameterizedTypeReference<List<Foo>>() {}) }
	}

	@Test
	fun awaitBody() {
		val response = mockk<ClientResponse>()
		every { response.bodyToMono<String>() } returns Mono.just("foo")
		runBlocking {
			assertThat(response.awaitBody<String>()).isEqualTo("foo")
		}
	}

	@Test
	fun awaitBodyOrNull() {
		val response = mockk<ClientResponse>()
		every { response.bodyToMono<String>() } returns Mono.empty()
		runBlocking {
			assertThat(response.awaitBodyOrNull<String>()).isNull()
		}
	}

	@Test
	fun awaitEntity() {
		val response = mockk<ClientResponse>()
		val entity = ResponseEntity("foo", HttpStatus.OK)
		every { response.toEntity<String>() } returns Mono.just(entity)
		runBlocking {
			assertThat(response.awaitEntity<String>()).isEqualTo(entity)
		}
	}

	@Test
	fun awaitEntityList() {
		val response = mockk<ClientResponse>()
		val entity = ResponseEntity(listOf("foo"), HttpStatus.OK)
		every { response.toEntityList<String>() } returns Mono.just(entity)
		runBlocking {
			assertThat(response.awaitEntityList<String>()).isEqualTo(entity)
		}
	}

	class Foo
}
