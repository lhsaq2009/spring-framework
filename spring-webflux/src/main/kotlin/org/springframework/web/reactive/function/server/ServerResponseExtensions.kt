package org.springframework.web.reactive.function.server

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitSingle
import org.reactivestreams.Publisher
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import reactor.core.publisher.Mono

/**
 * Extension for [ServerResponse.BodyBuilder.body] providing a `body(Publisher<T>)`
 * variant. This extension is not subject to type erasure and retains actual generic
 * type arguments.
 *
 * @author Sebastien Deleuze
 * @since 5.0
 */
inline fun <reified T : Any> ServerResponse.BodyBuilder.body(publisher: Publisher<T>): Mono<ServerResponse> =
		body(publisher, object : ParameterizedTypeReference<T>() {})

/**
 * Extension for [ServerResponse.BodyBuilder.body] providing a `body<T>(Any)` variant
 * leveraging Kotlin reified type parameters. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 * @param producer the producer to write to the response. This must be a
 * [Publisher] or another producer adaptable to a
 * [Publisher] via [org.springframework.core.ReactiveAdapterRegistry]
 * @param T the type of the elements contained in the producer
 * @author Sebastien Deleuze
 * @since 5.2
 */
inline fun <reified T : Any> ServerResponse.BodyBuilder.body(producer: Any): Mono<ServerResponse> =
		body(producer, object : ParameterizedTypeReference<T>() {})

/**
 * Coroutines variant of [ServerResponse.BodyBuilder.bodyValue].
 *
 * Set the body of the response to the given {@code Object} and return it.
 * This convenience method combines [body] and
 * [org.springframework.web.reactive.function.BodyInserters.fromValue].
 * @param body the body of the response
 * @return the built response
 * @throws IllegalArgumentException if `body` is a [Publisher] or an
 * instance of a type supported by [org.springframework.core.ReactiveAdapterRegistry.getSharedInstance],
 */
suspend fun ServerResponse.BodyBuilder.bodyValueAndAwait(body: Any): ServerResponse =
		bodyValue(body).awaitSingle()

/**
 * Coroutines variant of [ServerResponse.BodyBuilder.body] with [Any] and
 * [ParameterizedTypeReference] parameters providing a `bodyAndAwait(Flow<T>)` variant.
 * This extension is not subject to type erasure and retains actual generic type arguments.
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend inline fun <reified T : Any> ServerResponse.BodyBuilder.bodyAndAwait(flow: Flow<T>): ServerResponse =
		body(flow, object : ParameterizedTypeReference<T>() {}).awaitSingle()

/**
 * Extension for [ServerResponse.BodyBuilder.body] providing a
 * `bodyToServerSentEvents(Publisher<T>)` variant. This extension is not subject to type
 * erasure and retains actual generic type arguments.
 *
 * @author Sebastien Deleuze
 * @since 5.0
 */
@Deprecated("Use 'sse().body(publisher)' instead.", replaceWith = ReplaceWith("sse().body(publisher)"))
inline fun <reified T : Any> ServerResponse.BodyBuilder.bodyToServerSentEvents(publisher: Publisher<T>): Mono<ServerResponse> =
		contentType(MediaType.TEXT_EVENT_STREAM).body(publisher, object : ParameterizedTypeReference<T>() {})

/**
 * Shortcut for setting [MediaType.APPLICATION_JSON] `Content-Type` header.
 * @author Sebastien Deleuze
 * @since 5.1
 */
fun ServerResponse.BodyBuilder.json() = contentType(MediaType.APPLICATION_JSON)

/**
 * Shortcut for setting [MediaType.APPLICATION_XML] `Content-Type` header.
 * @author Sebastien Deleuze
 * @since 5.1
 */
fun ServerResponse.BodyBuilder.xml() = contentType(MediaType.APPLICATION_XML)

/**
 * Shortcut for setting [MediaType.TEXT_HTML] `Content-Type` header.
 * @author Sebastien Deleuze
 * @since 5.1
 */
fun ServerResponse.BodyBuilder.html() = contentType(MediaType.TEXT_HTML)

/**
 * Shortcut for setting [MediaType.TEXT_EVENT_STREAM] `Content-Type` header.
 * @author Sebastien Deleuze
 * @since 5.2
 */
fun ServerResponse.BodyBuilder.sse() = contentType(MediaType.TEXT_EVENT_STREAM)

/**
 * Coroutines variant of [ServerResponse.BodyBuilder.render].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun ServerResponse.BodyBuilder.renderAndAwait(name: String, vararg modelAttributes: String): ServerResponse =
		render(name, *modelAttributes).awaitSingle()

/**
 * Coroutines variant of [ServerResponse.BodyBuilder.render].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun ServerResponse.BodyBuilder.renderAndAwait(name: String, model: Map<String, *>): ServerResponse =
		render(name, model).awaitSingle()

/**
 * Coroutines variant of [ServerResponse.HeadersBuilder.build].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun ServerResponse.HeadersBuilder<out ServerResponse.HeadersBuilder<*>>.buildAndAwait(): ServerResponse =
		build().awaitSingle()

