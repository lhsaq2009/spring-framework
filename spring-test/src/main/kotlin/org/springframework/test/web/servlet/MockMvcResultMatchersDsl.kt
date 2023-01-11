package org.springframework.test.web.servlet

import org.hamcrest.Matcher
import org.springframework.test.web.servlet.result.*

/**
 * Provide a [MockMvcResultMatchers] Kotlin DSL in order to be able to write idiomatic Kotlin code.
 *
 * @author Sebastien Deleuze
 * @since 5.2
 */
class MockMvcResultMatchersDsl internal constructor (private val actions: ResultActions) {

	/**
	 * @see MockMvcResultMatchers.request
	 */
	fun request(matcher: RequestResultMatchers.() -> ResultMatcher) {
		actions.andExpect(MockMvcResultMatchers.request().matcher())
	}

	/**
	 * @see MockMvcResultMatchers.view
	 */
	fun view(matcher: ViewResultMatchers.() -> ResultMatcher) {
		actions.andExpect(MockMvcResultMatchers.view().matcher())
	}

	/**
	 * @see MockMvcResultMatchers.model
	 */
	fun model(matcher: ModelResultMatchers.() -> ResultMatcher) {
		actions.andExpect(MockMvcResultMatchers.model().matcher())
	}

	/**
	 * @see MockMvcResultMatchers.flash
	 */
	fun flash(matcher: FlashAttributeResultMatchers.() -> ResultMatcher) {
		actions.andExpect(MockMvcResultMatchers.flash().matcher())
	}

	/**
	 * @see MockMvcResultMatchers.forwardedUrl
	 */
	fun forwardedUrl(expectedUrl: String?) {
		actions.andExpect(MockMvcResultMatchers.forwardedUrl(expectedUrl))
	}

	/**
	 * @see MockMvcResultMatchers.forwardedUrlTemplate
	 */
	fun forwardedUrlTemplate(urlTemplate: String, vararg uriVars: Any) {
		actions.andExpect(MockMvcResultMatchers.forwardedUrlTemplate(urlTemplate, *uriVars))
	}

	/**
	 * @see MockMvcResultMatchers.forwardedUrlPattern
	 */
	fun forwardedUrlPattern(urlPattern: String) {
		actions.andExpect(MockMvcResultMatchers.forwardedUrlPattern(urlPattern))
	}

	/**
	 * @see MockMvcResultMatchers.redirectedUrl
	 */
	fun redirectedUrl(expectedUrl: String) {
		actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl))
	}

	/**
	 * @see MockMvcResultMatchers.redirectedUrlPattern
	 */
	fun redirectedUrlPattern(redirectedUrlPattern: String) {
		actions.andExpect(MockMvcResultMatchers.redirectedUrlPattern(redirectedUrlPattern))
	}

	/**
	 * @see MockMvcResultMatchers.status
	 */
	fun status(matcher: StatusResultMatchers.() -> ResultMatcher) {
		actions.andExpect(MockMvcResultMatchers.status().matcher())
	}

	/**
	 * @see MockMvcResultMatchers.header
	 */
	fun header(matcher: HeaderResultMatchers.() -> ResultMatcher) {
		actions.andExpect(MockMvcResultMatchers.header().matcher())
	}

	/**
	 * @see MockMvcResultMatchers.content
	 */
	fun content(matcher: ContentResultMatchers.() -> ResultMatcher) {
		actions.andExpect(MockMvcResultMatchers.content().matcher())
	}

	/**
	 * @see MockMvcResultMatchers.jsonPath
	 */
	fun <T> jsonPath(expression: String, matcher: Matcher<T>) {
		actions.andExpect(MockMvcResultMatchers.jsonPath(expression, matcher))
	}

	/**
	 * @see MockMvcResultMatchers.jsonPath
	 */
	fun jsonPath(expression: String, vararg args: Any, block: JsonPathResultMatchers.() -> ResultMatcher) {
		actions.andExpect(MockMvcResultMatchers.jsonPath(expression, *args).block())
	}

	/**
	 * @see MockMvcResultMatchers.xpath
	 */
	fun xpath(expression: String, vararg args: Any, namespaces: Map<String, String>? = null, xpathInit: XpathResultMatchers.() -> ResultMatcher) {
		actions.andExpect(MockMvcResultMatchers.xpath(expression, namespaces, args).xpathInit())
	}

	/**
	 * @see MockMvcResultMatchers.cookie
	 */
	fun cookie(cookieInit: CookieResultMatchers.() -> ResultMatcher) {
		val cookie = MockMvcResultMatchers.cookie().cookieInit()
		actions.andExpect(cookie)
	}

	/**
	 * @see ResultActions.andExpect
	 */
	fun match(matcher: ResultMatcher) {
		actions.andExpect(matcher)
	}
}
