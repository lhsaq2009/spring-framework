package org.springframework.web.reactive.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.http.codec.multipart.Part
import org.springframework.util.MultiValueMap
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebSession
import java.security.Principal

/**
 * Coroutines variant of [ServerWebExchange.getFormData].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun ServerWebExchange.awaitFormData(): MultiValueMap<String, String> =
		this.formData.awaitSingle()

/**
 * Coroutines variant of [ServerWebExchange.getMultipartData].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun ServerWebExchange.awaitMultipartData(): MultiValueMap<String, Part> =
		this.multipartData.awaitSingle()

/**
 * Coroutines variant of [ServerWebExchange.getPrincipal].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun <T : Principal> ServerWebExchange.awaitPrincipal(): T =
		this.getPrincipal<T>().awaitSingle()

/**
 * Coroutines variant of [ServerWebExchange.getSession].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
suspend fun ServerWebExchange.awaitSession(): WebSession =
		this.session.awaitSingle()

/**
 * Coroutines variant of [ServerWebExchange.Builder.principal].
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
fun ServerWebExchange.Builder.principal(supplier: suspend () -> Principal): ServerWebExchange.Builder
        = principal(mono(Dispatchers.Unconfined) { supplier.invoke() })
