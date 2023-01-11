package org.springframework.web.reactive.function.server

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

class RenderingResponseExtensionsTests {

	@Test
	fun buildAndAwait() {
		val builder = mockk<RenderingResponse.Builder>()
		val response = mockk<RenderingResponse>()
		every { builder.build() } returns Mono.just(response)
		runBlocking {
			builder.buildAndAwait()
		}
		verify {
			builder.build()
		}
	}

}