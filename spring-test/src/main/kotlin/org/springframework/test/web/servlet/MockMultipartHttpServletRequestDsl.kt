package org.springframework.test.web.servlet

import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder
import javax.servlet.http.Part

/**
 * Provide a [MockMultipartHttpServletRequestBuilder] Kotlin DSL in order to be able to write idiomatic Kotlin code.
 *
 * @see MockMvc.multipart
 * @author Sebastien Deleuze
 * @since 5.2
 */
class MockMultipartHttpServletRequestDsl internal constructor (private val builder: MockMultipartHttpServletRequestBuilder) : MockHttpServletRequestDsl(builder) {

	/**
	 * @see [MockMultipartHttpServletRequestBuilder.file]
	 */
	fun file(name: String, content: ByteArray) = builder.file(name, content)

	/**
	 * @see [MockMultipartHttpServletRequestBuilder.file]
	 */
	fun file(file: MockMultipartFile) = builder.file(file)

	/**
	 * @see [MockMultipartHttpServletRequestBuilder.part]
	 */
	fun part(vararg parts: Part) = builder.part(*parts)
}
